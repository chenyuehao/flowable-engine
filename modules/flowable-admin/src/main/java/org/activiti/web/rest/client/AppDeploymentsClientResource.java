/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.web.rest.client;

import com.fasterxml.jackson.databind.JsonNode;

import org.activiti.domain.EndpointType;
import org.activiti.domain.ServerConfig;
import org.activiti.service.engine.AppService;
import org.activiti.service.engine.exception.ActivitiServiceException;
import org.activiti.web.rest.exception.BadRequestException;
import org.activiti.web.rest.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * @author Yvo Swillens
 */
@RestController
@RequestMapping("/rest/activiti/apps")
public class AppDeploymentsClientResource extends AbstractClientResource {

    private final Logger log = LoggerFactory.getLogger(AppDeploymentsClientResource.class);

    @Autowired
    protected AppService clientService;

    /**
     * GET  /rest/activiti/apps -> get a list of apps.
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public JsonNode listDeployments(HttpServletRequest request) {
        log.debug("REST request to get a list of apps");
        
        JsonNode resultNode = null;
        ServerConfig serverConfig = retrieveServerConfig(EndpointType.PROCESS);
    	Map<String, String[]> parameterMap = getRequestParametersWithoutServerId(request);
    	
    	try {
    		resultNode = clientService.listAppDefinitions(serverConfig, parameterMap);
	        
        } catch (ActivitiServiceException e) {
        	throw new BadRequestException(e.getMessage());
        }
    	
        return resultNode;
    }
    
    /**
     * POST /rest/activiti/apps: upload a app
     */
    @RequestMapping(method=RequestMethod.POST)
    public void handleFileUpload(HttpServletRequest request, HttpServletResponse httpResponse, @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
        	try {
        		ServerConfig serverConfig = retrieveServerConfig(EndpointType.PROCESS);
        		String fileName = file.getOriginalFilename();
        		if (fileName != null && fileName.endsWith(".zip")) {
        			clientService.uploadAppDefinition(httpResponse, serverConfig, fileName, file.getInputStream());
        		} else {
        			throw new BadRequestException("Invalid file name");
        		}
            } catch (IOException e) {
            	throw new InternalServerErrorException("Could not deploy file: " + e.getMessage());
            }
        } else {
            throw new BadRequestException("No file found in POST body");
        }
    }
}
