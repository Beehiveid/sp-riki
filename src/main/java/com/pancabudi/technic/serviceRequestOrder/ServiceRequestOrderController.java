package com.pancabudi.technic.serviceRequestOrder;

import com.pancabudi.technic.exception.ConsentFlowException;
import com.pancabudi.technic.person.Person;
import com.pancabudi.technic.person.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/sro")
@Api(description = "Operation that handle Service Request Order (SRO) module", tags = "SRO")
public class ServiceRequestOrderController {
    @Autowired
    private ServiceRequestOrderService serviceRequestOrderService;

    @Autowired
    private PersonService personService;

    @GetMapping
    @ApiOperation(
            value = "Show list of SRO"
    )
    public List<ServiceRequestOrder> index(){
        return serviceRequestOrderService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Show an SRO by an id"
    )
    public ServiceRequestOrder findById(
            @ApiParam(value = "SRO's id that will be displayed") @PathVariable String id){
        return serviceRequestOrderService.findById(id);
    }

    @PostMapping
    @ApiOperation(
            value = "Submit new SRO"
    )
    public void create(
            @ApiParam(value = "SRO that will be submitted") @RequestBody ServiceRequestOrder serviceRequestOrder){
        Person requester = personService.getLogged();
        boolean isOkay = serviceRequestOrderService.consentFlow("create", requester);

        if(!isOkay)
            throw new ConsentFlowException("SRO");

        serviceRequestOrder.setRequester(requester);
        serviceRequestOrderService.submit(serviceRequestOrder, false);
    }

    @GetMapping("/{id}/handling")
    @ApiOperation(
            value = "Show list of SRO handling"
    )
    public List<ServiceRequestOrderHandling> handlingList(
            @ApiParam(value = "SRO's id that will be displayed its handling") @PathVariable String id){
        return serviceRequestOrderService.handlingList(id);
    }

    @PostMapping("/{id}/handling")
    @ApiOperation(
            value = "Submit an SRO handling"
    )
    public void handlingSubmit(
            @ApiParam(value = "SRO's id that will be submitted a handling") @PathVariable String id,
            @ApiParam(value = "Handling that will be submitted related to SRO's id") @RequestBody ServiceRequestOrderHandling logHandling){
        Person handler = logHandling.getHandler();

        boolean isOkay = serviceRequestOrderService.consentFlow("handle", handler);

        if(!isOkay)
            throw new ConsentFlowException("SRO");

        serviceRequestOrderService.handlingSubmit(id,logHandling);
    }

    @GetMapping("/dataTables")
    @ApiOperation(
            value = "Show list of SRO in DataTables format output"
    )
    public DataTablesOutput<ServiceRequestOrder> index(@Valid DataTablesInput input, HttpServletRequest request){
        String scope = String.valueOf(request.getAttribute("scope"));

        if(scope.equals("KEPALA_REGU")){
            return serviceRequestOrderService.findAll(input, 9,7,8);
        }else{
            return serviceRequestOrderService.findAll(input, false);
        }
    }

    @GetMapping("/monitoring")
    @ApiOperation(
            value = "SRO Monitoring"
    )
    public DataTablesOutput<ServiceRequestOrder> monitoring(@Valid DataTablesInput input,@RequestParam(name = "location", required = false, defaultValue = "0") Long location, HttpServletResponse response, HttpServletRequest request){
        response.setHeader("Access-Control-Allow-Origin", "*");

        if(location == 0){
            request.setAttribute("pid", 1L);
            return serviceRequestOrderService.findAll(input, true);
        }else{
            return serviceRequestOrderService.findAll(input, location);
        }
    }

    @PreAuthorize("hasRole('KEPALA_REGU') OR hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    @ApiOperation(
            value = "Update an SRO"
    )
    public void update(
            @ApiParam(value = "SRO's id that will be updated") @PathVariable String id,
            @ApiParam(value = "SRO that will be updated") @RequestBody ServiceRequestOrder serviceRequestOrder){
        serviceRequestOrderService.update(id, serviceRequestOrder);
    }

    @PutMapping("/{id}/cancel")
    @ApiOperation(
            value = "Cancel an SRO"
    )
    public void cancel(
            @ApiParam(value = "SRO's id that will be canceled") @PathVariable String id){
        Person canceller = personService.getLogged();
        boolean isOkay = serviceRequestOrderService.consentFlow("cancel", canceller);

        if(!isOkay)
            throw new ConsentFlowException("SRO");

        serviceRequestOrderService.cancel(id, canceller);
    }

    @GetMapping("/report")
    public ResponseEntity<HashMap<String, String>> report(@RequestParam String start, @RequestParam String end, @RequestParam int type) throws IOException, ParseException {
        File report = serviceRequestOrderService.report(start, end, type);

        byte[] bytes = Files.readAllBytes(report.toPath());
        String encoded = Base64.getEncoder().encodeToString(bytes);

        Files.deleteIfExists(report.toPath());

        HashMap<String,String> resp = new HashMap<>();

        resp.put("fileName", report.getName());
        resp.put("file", encoded);

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+report.getName()+"\"")
                .body(resp);
    }

    @PatchMapping("/{id}/pick")
    public void pick(@PathVariable String id){
        Person picker = personService.getLogged();

        boolean isOkay = serviceRequestOrderService.consentFlow("pick", picker);

        if(!isOkay)
            throw new ConsentFlowException("SRO");

        serviceRequestOrderService.pick(id,picker);
    }

    @PatchMapping("/{id}/accept")
    public void accept(@PathVariable String id){
        Person acceptor = personService.getLogged();

        boolean isOkay = serviceRequestOrderService.consentFlow("close", acceptor);

        if(!isOkay)
            throw new ConsentFlowException("SRO");

        serviceRequestOrderService.accept(id, acceptor);
    }

    @PostMapping("/consent/{code}")
    public void postConsent(@PathVariable String code){
        serviceRequestOrderService.postConsent(code);
    }

    @GetMapping("/machine")
    public HashMap<String, List<Object>> getMachine(){
        return serviceRequestOrderService.getRelevantMachine();
    }
}
