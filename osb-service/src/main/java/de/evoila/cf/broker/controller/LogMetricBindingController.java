package de.evoila.cf.broker.controller;

import de.evoila.cf.broker.exception.ServiceInstanceDoesNotExistException;
import de.evoila.cf.broker.model.ServiceInstanceBinding;
import de.evoila.cf.broker.repository.BindingRepository;
import de.evoila.cf.broker.repository.ServiceInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reneschollmeyer, evoila on 17.05.18.
 */
@RestController
@RequestMapping(value = "/v2/service_instances")
public class LogMetricBindingController {

    private static final String mapKey = "service_bindings";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BindingRepository bindingRepository;

    @Autowired
    private ServiceInstanceRepository serviceInstanceRepository;

    @GetMapping(value = "/{instanceId}/service_bindings")
    public ResponseEntity<Map<String, List<String>>> getServiceBindings(@PathVariable("instanceId") String instanceId) throws ServiceInstanceDoesNotExistException {

        log.debug("GET: /v2/service_instances/{instanceId}/service_bindings"
                + ", getServiceBindings(), serviceInstanceId = " + instanceId);

        if(serviceInstanceRepository.containsServiceInstanceId(instanceId)) {
            Map<String, List<String>> appIds = new HashMap<>();

            appIds.put(mapKey, new ArrayList<>());

            for(ServiceInstanceBinding serviceInstanceBinding: bindingRepository.getBindingsForServiceInstance(instanceId)) {
                appIds.get(mapKey).add(serviceInstanceBinding.getAppGuid());
            }

            return new ResponseEntity<>(appIds, HttpStatus.OK);
        } else {
            throw new ServiceInstanceDoesNotExistException(instanceId);
        }
    }
}