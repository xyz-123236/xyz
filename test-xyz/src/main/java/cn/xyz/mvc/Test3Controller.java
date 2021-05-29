package cn.xyz.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl;
import com.atlassian.crowd.integration.http.filter.CrowdSecurityFilter;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelper;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractor;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.ClientResourceLocator;
import com.atlassian.crowd.service.client.CrowdClient;

import cn.xyz.common.annotation.*;

@Controller()
@RequestMapping("t3")
public class Test3Controller {

    @Autowired
    private TestService testService;

    @RequestMapping("t3")
    public String myTest(JSONObject obj){
    	ClientResourceLocator resourceLocator = new ClientResourceLocator("crowd.properties");
		ClientPropertiesImpl clientProperties = ClientPropertiesImpl.newInstanceFromResourceLocator(resourceLocator);
		RestCrowdClientFactory crowdClientFactory = new RestCrowdClientFactory();
		CrowdClient crowdClient = crowdClientFactory.newInstance(clientProperties);
		CrowdHttpValidationFactorExtractor validationFactorExtractor = CrowdHttpValidationFactorExtractorImpl.getInstance();
		CrowdHttpTokenHelper tokenHelper = CrowdHttpTokenHelperImpl.getInstance(validationFactorExtractor);
		
		CrowdHttpAuthenticator crowdHttpAuthenticator = new CrowdHttpAuthenticatorImpl(crowdClient, clientProperties, tokenHelper);
		CrowdSecurityFilter crowdSecurityFilter = new CrowdSecurityFilter(crowdHttpAuthenticator,clientProperties);
		
		com.atlassian.crowd.model.user.User crowdUser = null;
		try {
			crowdUser = crowdClient.authenticateUser("tang.wu", "Tw*0133363");
			System.out.println(3);
			//crowdUser = crowdHttpAuthenticator.authenticate(request, response, "tang.wu", "Tw*0133363");
			List<Group> groups = crowdClient.getGroupsForUser(crowdUser.getName(), 0, -1);;
			for(Group group : groups) {
				System.out.println(group.getName());
			}
			System.out.println(crowdUser.getName());
			System.out.println(crowdUser.getLastName());
			System.out.println(crowdUser.getFirstName());
			System.out.println(crowdUser.getLastName());
			System.out.println(crowdUser.getEmailAddress());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "login.jsp";
    }
    
    public static void main(String[] args) {
    	ClientResourceLocator resourceLocator = new ClientResourceLocator("crowd.properties");
		ClientPropertiesImpl clientProperties = ClientPropertiesImpl.newInstanceFromResourceLocator(resourceLocator);
		RestCrowdClientFactory crowdClientFactory = new RestCrowdClientFactory();
		CrowdClient crowdClient = crowdClientFactory.newInstance(clientProperties);
		CrowdHttpValidationFactorExtractor validationFactorExtractor = CrowdHttpValidationFactorExtractorImpl.getInstance();
		CrowdHttpTokenHelper tokenHelper = CrowdHttpTokenHelperImpl.getInstance(validationFactorExtractor);
		
		CrowdHttpAuthenticator crowdHttpAuthenticator = new CrowdHttpAuthenticatorImpl(crowdClient, clientProperties, tokenHelper);
		CrowdSecurityFilter crowdSecurityFilter = new CrowdSecurityFilter(crowdHttpAuthenticator,clientProperties);
		
		com.atlassian.crowd.model.user.User crowdUser = null;
		try {
			crowdUser = crowdClient.authenticateUser("tang.wu", "Xyz-123236");
			System.out.println(3);
			//crowdUser = crowdHttpAuthenticator.authenticate(request, response, "tang.wu", "Tw*0133363");
			List<Group> groups = crowdClient.getGroupsForUser("tang.wu", 0, -1);
			List<User> list = crowdClient.getUsersOfGroup("portal-purchase", 0, -1);
			for (int i = 0; i < list.size(); i++) {
				System.out.println(JSON.toJSONString(list.get(i)));
			}
			for(Group group : groups) {
				System.out.println(group.getName());
			}
			System.out.println(crowdUser.getName());
			System.out.println(crowdUser.getLastName());
			System.out.println(crowdUser.getFirstName());
			System.out.println(crowdUser.getLastName());
			System.out.println(crowdUser.getEmailAddress());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}

