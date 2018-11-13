package com.linguancheng.gdeiassistant.Controller.UserLogin;

import com.linguancheng.gdeiassistant.Pojo.Entity.Token;
import com.linguancheng.gdeiassistant.Pojo.Result.JsonResult;
import com.linguancheng.gdeiassistant.Pojo.TokenRefresh.TokenRefreshJsonResult;
import com.linguancheng.gdeiassistant.Pojo.TokenRefresh.TokenRefreshResult;
import com.linguancheng.gdeiassistant.Service.IPAddress.IPService;
import com.linguancheng.gdeiassistant.Service.Token.LoginTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TokenRestController {

    @Autowired
    private LoginTokenService loginTokenService;

    @Autowired
    private IPService ipService;

    /**
     * 使令牌信息主动过期
     *
     * @param signature
     * @return
     */
    @RequestMapping(value = "/rest/token/expire", method = RequestMethod.POST)
    public JsonResult ExpireToken(@RequestParam("token") String signature) throws Exception {
        JsonResult result = new JsonResult();
        loginTokenService.ExpireToken(signature);
        result.setSuccess(true);
        return result;
    }

    /**
     * 刷新登录令牌
     *
     * @param request
     * @param refreshTokenSignature
     * @return
     */
    @RequestMapping(value = "/rest/token/refresh", method = RequestMethod.POST)
    public TokenRefreshJsonResult RefreshToken(HttpServletRequest request
            , @RequestParam("token") String refreshTokenSignature) throws Exception {
        TokenRefreshJsonResult result = new TokenRefreshJsonResult();
        //获取用户请求的IP地址
        String ip = ipService.GetRequestRealIPAddress(request);
        //刷新令牌
        TokenRefreshResult tokenRefreshResult = loginTokenService.RefreshToken(refreshTokenSignature, ip);
        result.setAccessToken(new Token(tokenRefreshResult.getAccessToken()));
        result.setRefreshToken(new Token(tokenRefreshResult.getRefreshToken()));
        return result;
    }

}
