/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.security.main;

import co.com.foundation.soaint.security.domain.AuthenticationResponseContext;
import co.com.foundation.soaint.security.domain.PrincipalContext;
import co.com.foundation.soaint.security.interfaces.SecurityAuthenticator;
import co.com.foundation.soaint.security.ldap.mapper.PrincipalMapper;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

public class Main {

    public static void main(String[] args) throws Exception {

        System.setProperty("spring.profiles.active", "dev");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/sc-ldap-datasources-config.xml", "/spring/sc-integration-config.xml");


        SecurityAuthenticator sa = (SecurityAuthenticator) ctx.getBean("LDAPSecurityAuthenticator");

        System.out.println(sa.login("j.rodriguez", "qwerty"));

        AuthenticationResponseContext context = sa.login("j.rodriguez", "qwerty");


    }


}
