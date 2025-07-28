import { PaymentResponse } from '@/types/payment';
import { parseCookies } from 'nookies';

const API_BASE_URL = 'http://localhost:8080';

export const getActualBilling = async (monthRequest: string): Promise<PaymentResponse[]> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;
    const cardId = cookies.cardId;
    if (!cardId) throw new Error('No cardId found.');

    if (!token) {
      throw new Error('No authentication token found.');
    }

    const response = await fetch(
      `${API_BASE_URL}/bia/payments/billing/${cardId}?month=${monthRequest}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      },
    );

    if (!response.ok) {
      let errorMessage = `Failed to get Billing (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {}
      throw new Error(errorMessage);
    }

    const data: PaymentResponse[] = await response.json();
    console.log(data);
    return data;
  } catch (error: any) {
    console.error('Error fetching billing:', error);
    throw error;
  }
};
// export const payInstallment = async (paymentValue: number): Promise<> => {
//   try {
//     const cookies = parseCookies();
//     const token = cookies.token;
//     const accountNumber = cookies.accountNumber;
//     if (!accountNumber) throw new Error('No accountNumber found.');
//
//     if (!token) {
//       throw new Error('No authentication token found.');
//     }
//
//     const response = await fetch(`${API_BASE_URL}/bia/cards/add/${accountNumber}`, {
//       method: 'POST',
//       headers: {
//         'Content-Type': 'application/json',
//         Authorization: `Bearer ${token}`,
//       },
//     });
//
//     if (!response.ok) {
//       let errorMessage = `Failed to create card (status ${response.status})`;
//       try {
//         const error = await response.json();
//         errorMessage = error.message || errorMessage;
//       } catch {}
//       throw new Error(errorMessage);
//     }
//
//   } catch (error: any) {
//     console.error('Error creating account:', error);
//     throw error;
//   }
// };
export const payActualBilling = async (monthRequest: string): Promise<void> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;
    const cardId = cookies.cardId;
    if (!cardId) throw new Error('No cardId found.');

    if (!token) {
      throw new Error('No authentication token found.');
    }

    const response = await fetch(`${API_BASE_URL}/bia/cards/credit/${cardId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(monthRequest),
    });

    if (!response.ok) {
      let errorMessage = `Failed to create pay billing (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {}
      throw new Error(errorMessage);
    }
  } catch (error: any) {
    console.error('Error creating account:', error);
    throw error;
  }
};
