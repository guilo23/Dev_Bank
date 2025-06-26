import { CustomerAccount } from '@/types/customer';

const API_BASE_URL = 'http://localhost:8080';

export const register = async (registerRequest: CustomerAccount, customerId: String): Promise<CustomerAccount> => {
  try {
    const response = await fetch(`${API_BASE_URL}/bia/account/${customerId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(registerRequest),
    });
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || `Failed to authenticate customer (status ${response.status})`);
    }
    const data: CustomerAccount = await response.json();
    return data;
  } catch (error: any) {
    console.error("error to create account", error);
    throw error;
  }
};

