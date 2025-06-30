import { accountRequest, accountResponse } from '@/types/account';
import { parseCookies } from 'nookies';

const API_BASE_URL = 'http://localhost:8080';

export const registerAccount = async (
  registerRequest: accountRequest,
  customerId: number
): Promise<accountResponse> => {
  try {
    const cookies = parseCookies();
    const token = cookies.token;

    if (!token) {
      throw new Error("No authentication token found.");
    }

    const response = await fetch(`${API_BASE_URL}/bia/account/${customerId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(registerRequest),
    });

    if (!response.ok) {
      let errorMessage = `Failed to create account (status ${response.status})`;
      try {
        const error = await response.json();
        errorMessage = error.message || errorMessage;
      } catch {
      }
      throw new Error(errorMessage);
    }

    const data: accountResponse = await response.json();
    return data;

  } catch (error: any) {
    console.error("Error creating account:", error);
    throw error;
  }
};
