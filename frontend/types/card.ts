export interface cardRequest {
  cardType: string;
  cardNumber: string;
  cardLimit: number;
}
export interface cardResponse {
  cardId: string;
  cardNumber: string;
  cardLimit: string;
  customername: string;
}
export interface paymentResponse {
  cardNumber: string;
  productName: string;
  installmentNumber: number;
  installmentAmount: number;
  paymentDate: Date;
}
