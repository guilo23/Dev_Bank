import { accountRequest, accountResponse } from '@/types/account';

const API_BASE_URL = 'http://localhost:8080';
const token = localStorage.getItem("token");

export const registerAccount = async (registerRequest: accountRequest, customerId: number): Promise<accountResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/bia/account/${customerId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(registerRequest),
    });
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || `Failed to authenticate customer (status ${response.status})`);
    }
    const data: accountResponse = await response.json();
    return data;
  } catch (error: any) {
    console.error("error to create account", error);
    throw error;
  }
};

