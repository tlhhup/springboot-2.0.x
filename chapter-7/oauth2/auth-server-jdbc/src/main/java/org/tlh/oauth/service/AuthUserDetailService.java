package org.tlh.oauth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/6
 * <p>
 * Github: https://github.com/tlhhup
 */
@Slf4j
@Service
public class AuthUserDetailService implements UserDetailsService {

    private static final String DEF_USER_EXISTS_SQL = "select username,password,enabled from users where username = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> users = this.jdbcTemplate.query(this.DEF_USER_EXISTS_SQL,
                new String[]{username}, new RowMapper<UserDetails>() {
                    @Override
                    public UserDetails mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        String username = rs.getString(1);
                        String password = rs.getString(2);
                        boolean enabled = rs.getBoolean(3);
                        return new User(username, password, enabled, true, true, true,
                                AuthorityUtils.NO_AUTHORITIES);
                    }

                });
        if (users.size() == 0) {
            log.debug("Query returned no results for user '" + username + "'");

            throw new UsernameNotFoundException(
                    this.messages.getMessage("JdbcDaoImpl.notFound",
                            new Object[] { username }, "Username {0} not found"));
        }
        UserDetails user = users.get(0);
        return user;
    }
}
