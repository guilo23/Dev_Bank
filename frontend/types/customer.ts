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
  cpf: string;
  phoneNumber: string;
  accountType: string;
  currentBalance: number;
}
