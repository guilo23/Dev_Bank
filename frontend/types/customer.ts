export interface CustomerLogin {
  email: string;
  password: string;
}
export interface LoginData {
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
export interface CustomerResponse{
  id: number;
}
