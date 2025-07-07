import { transactionResponse } from '@/types/transaction';
import { parseCookies } from 'nookies';

const API_BASE_URL = 'http://localhost:8080';

export const getTransactions = async (): Promise<transactionResponse[]> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;
    const accountNumber = cookies.accountNumber;

    if (!token) {
      throw new Error('No authentication token found.');
    }
    const response = await fetch(
      `${API_BASE_URL}/bia/transactions/by-account?accountNumber=${accountNumber}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      },
    );
    if (!response.ok) {
      let errorMessage = `Failed to  (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {}
      throw new Error(errorMessage);
    }
    const data: transactionResponse[] = await response.json();
    return data;
  } catch (error: any) {
    console.error('Error fecth transaction:', error);
    throw error;
  }
};
