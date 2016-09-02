package com.pwc.assurance.adc.config.audit;

import com.pwc.assurance.adc.config.Constants;
import com.pwc.assurance.adc.security.SecurityUtils;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.stereotype.Component;

@Component
public class JaversAuthorProvider implements AuthorProvider {

   @Override
   public String provide() {
       String userName = SecurityUtils.getCurrentUserLogin();
       return (userName != null ? userName : Constants.SYSTEM_ACCOUNT);
   }
}
