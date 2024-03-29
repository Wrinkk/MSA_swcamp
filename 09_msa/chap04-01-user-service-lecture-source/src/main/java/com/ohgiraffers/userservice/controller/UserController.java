package com.ohgiraffers.userservice.controller;

import com.ohgiraffers.userservice.client.OrderServiceClient;
import com.ohgiraffers.userservice.dto.UserDTO;
import com.ohgiraffers.userservice.service.UserService;
import com.ohgiraffers.userservice.vo.HelloVO;
import com.ohgiraffers.userservice.vo.RequestUser;
import com.ohgiraffers.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final Environment env;
    private final HelloVO helloVO;

    private final ModelMapper modelMapper;

    private final UserService userService;    // UserServiceImpl을 보지않고 UserService를 봄 (타입은닉 기술)

    private final OrderServiceClient orderServiceClient;

    /* 설명. FeignClient 이후 추가 할 부분 */

    @Autowired
    public UserController(Environment env, HelloVO helloVO, ModelMapper modelMapper, UserService userService, OrderServiceClient orderServiceClient) {
        this.env = env;
        this.helloVO = helloVO;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.orderServiceClient = orderServiceClient;
    }
    /* 설명.
     *  application.yml 파일로부터 설정 값을 불러오기 위해서는 두 가지 방법이 제공된다.
     *  1. Environment를 의존성 주입 받아 getProperty로 설정 키 값을 작성해 불러오는 방법
     *  2. @Value를 활용해 필드로 주입 받고 활용하는 방법
     * */

    /* 설명. 1. Environment 활용해 설정값 불러오기 */
    @GetMapping("/health_check")
    public String status() {
        return "Server at " + env.getProperty("local.server.port")
                + ", swcamp.message: " + env.getProperty("swcamp.message");

    }

    /* 설명. 2. @Value 활용해 설정값 불러오기(getter) */
    @GetMapping("/do_msa")
    public String doMsa() {
        return helloVO.getMessage();
    }

    /* 설명. 회원가입(POST - /users) */
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> registUser(@RequestBody RequestUser user) {

        /* 설명. config server에서 제공하는 test.message값 확인 */
        System.out.println("config server의 설정 값 확인: "
                + env.getProperty("test.message"));

        /* 설명. RequestUser -> UserDTO */
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        /* 설명. 회원가입 비즈니스 로직 시작 */
        userService.registUser(userDTO);        // call by reference

        /* 설명. UserDTO -> ResponseUser */
        ResponseUser responseUser = modelMapper.map(userDTO, ResponseUser.class);


        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("id") String id) {
        UserDTO userDTO = userService.getUserById(id);

        ResponseUser returnValue = modelMapper.map(userDTO, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
