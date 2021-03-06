package me.whiteship.demospringsecurityform.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    @WithAnonymousUser
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @WithUser
    public void index_user() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    public void admin_user() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAdmin
    public void admin_admin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void login_success() throws Exception{
        Account user = this.createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("123"))
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    public void login_fail() throws Exception{
        Account user = this.createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("12345"))
                .andExpect(unauthenticated());
    }

    private Account createUser() {
        Account account = new Account();
        account.setUsername("fe26ming");
        account.setPassword("123");
        account.setRole("USER");
        accountService.createNew(account);
        return account;
    }
}