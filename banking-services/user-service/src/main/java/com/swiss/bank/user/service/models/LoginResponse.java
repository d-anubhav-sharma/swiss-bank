package com.swiss.bank.user.service.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

  private String username;
  private String token;
  private List<String> roles;
  private String nationality;
  
}
