import { cardRequest, cardResponse, paymentResponse } from '@/types/card';
import { parseCookies } from 'nookies';

const API_BASE_URL = 'http://localhost:8080';

export const RequestCard = async (registerRequest: cardRequest): Promise<cardResponse> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;
    const accountNumber = cookies.accountNumber;
    if (!accountNumber) throw new Error('No accountNumber found.');

    if (!token) {
      throw new Error('No authentication token found.');
    }

    const response = await fetch(`${API_BASE_URL}/bia/cards/add/${accountNumber}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(registerRequest),
    });

    if (!response.ok) {
      let errorMessage = `Failed to create card (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {}
      throw new Error(errorMessage);
    }

    const data: cardResponse = await response.json();
    return data;
  } catch (error: any) {
    console.error('Error creating account:', error);
    throw error;
  }
};
export const getCards = async (): Promise<cardResponse[]> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;
    const accountNumber = cookies.accountNumber;
    if (!accountNumber) throw new Error('No accountNumber found.');

    if (!token) {
      throw new Error('No authentication token found.');
    }

    const response = await fetch(`${API_BASE_URL}/bia/cards/list/${accountNumber}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      let errorMessage = `Failed to create accountNumber (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {}
      throw new Error(errorMessage);
    }

    const data: cardResponse[] = await response.json();
    console.log(data);
    return data;
  } catch (error: any) {
    console.error('Error fetching cards:', error);
    throw error;
  }
};
export const getcardById = async (): Promise<cardResponse> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;
    const cardId = cookies.cardId;
    if (!cardId) throw new Error('No cardId found.');

    if (!token) {
      throw new Error('No authentication token found.');
    }

    const response = await fetch(`${API_BASE_URL}/bia/cards/${cardId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      let errorMessage = `Failed to create account (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {}
      throw new Error(errorMessage);
    }

    const data: cardResponse = await response.json();
    return data;
  } catch (error: any) {
    console.error('Error fecth card:', error);
    throw error;
  }
};
export const getcardPayments = async (): Promise<paymentResponse> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;
    const cardId = cookies.cardId;
    if (!cardId) throw new Error('No cardId found.');

    if (!token) {
      throw new Error('No authentication token found.');
    }

    const response = await fetch(`${API_BASE_URL}/bia/payments/card-payments?cardId=${cardId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      let errorMessage = `Failed to create account (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {}
      throw new Error(errorMessage);
    }

    const data: paymentResponse = await response.json();
    return data;
  } catch (error: any) {
    console.error('Error fecth card:', error);
    throw error;
  }
};
