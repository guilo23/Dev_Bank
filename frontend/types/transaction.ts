export interface transactionRequest {}
export interface transactionResponse {
  amount: number;
  receiverName: string;
  senderName: string;
  transactionDate: Date;
}
