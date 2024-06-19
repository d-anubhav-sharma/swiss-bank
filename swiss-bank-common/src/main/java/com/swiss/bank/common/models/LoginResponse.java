package com.swiss.bank.common.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {

  private String username;
  private String token;
  private List<String> roles;
  private String nationality;
  
}
