package com.mb.lab.banks.auth.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Controller for retrieving the model for and displaying the confirmation page for access to a protected resource.
 */
@Controller
@SessionAttributes("authorizationRequest")
public class AuthController {

//    @Autowired
//    private ClientDetailsService clientDetailsService;

//    @Autowired
//    private ApprovalStore approvalStore;

//    @RequestMapping("/oauth/confirm_access")
//    public ModelAndView getAccessConfirmation(Map<String, Object> model, Principal principal) {
//        AuthorizationRequest clientAuth = (AuthorizationRequest) model.remove("authorizationRequest");
//        ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
//        model.put("auth_request", clientAuth);
//        model.put("client", client);
//        Map<String, String> scopes = new LinkedHashMap<String, String>();
//        for (String scope : clientAuth.getScope()) {
//            scopes.put(OAuth2Utils.SCOPE_PREFIX + scope, "false");
//        }
//        for (Approval approval : approvalStore.getApprovals(principal.getName(), client.getClientId())) {
//            if (clientAuth.getScope().contains(approval.getScope())) {
//                scopes.put(OAuth2Utils.SCOPE_PREFIX + approval.getScope(), approval.getStatus() == ApprovalStatus.APPROVED ? "true" : "false");
//            }
//        }
//        model.put("scopes", scopes);
//        return new ModelAndView("oauth_access_confirmation", model);
//    }

    @RequestMapping("/oauth/error")
    public String handleError(Map<String, Object> model) {
        // We can add more stuff to the model here for JSP rendering. If the
        // client was a machine then
        // the JSON will already have been rendered.
        model.put("message", "There was a problem with the OAuth2 protocol");
        return "oauth_error";
    }

}
