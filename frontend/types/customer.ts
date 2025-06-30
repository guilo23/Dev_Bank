export interface CustomerLogin {
  email: string;
  password: string;
}
export interface LoginData {
  id: number;
  token: string;
}
export interface CustomerAccount {
  name: string;
  email: string;
  password: string;
  birthday: string;
  CPF: string;
  phoneNumber: string;
}
export interface JwtPayload {
  customerId: number;
  sub: string;
  exp: number;
  iat: number;
}
export interface CustomerResponse {
  id: number;
}
