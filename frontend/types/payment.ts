export interface PaymentRequest {
  email: string;
  password: string;
}
export interface PaymentResponse {
  id: number;
  token: string;
}
