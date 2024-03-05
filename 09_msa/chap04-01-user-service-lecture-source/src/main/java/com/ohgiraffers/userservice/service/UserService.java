package com.ohgiraffers.userservice.service;

import com.ohgiraffers.userservice.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    void registUser(UserDTO userDTO);

    UserDTO getUserDetailsByEmail(String email);

    /* 설명. UserDetailsService 인터페이스 상속 이후 DB에서 로그인 사용자 정보 조회 후 UserDetails 타입으로 반환하는 기능 구현 */
}
