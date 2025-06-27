import { CustomerLogin, LoginData, CustomerAccount,CustomerResponse } from '@/types/customer';

const API_BASE_URL = 'http://localhost:8080';

export const login = async (loginRequest: CustomerLogin): Promise<LoginData> => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginRequest),
    });
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || `Failed to authenticate customer (status ${response.status})`);
    }
    const data: LoginData = await response.json();
    return data;
  } catch (error: any) {
    console.error("Error logging in:", error);
    throw error;
  }
};
export const register = async (registerRequest: CustomerAccount): Promise<CustomerResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/bia/customer`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(registerRequest),
    });
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || `Failed to create customer (status ${response.status})`);
    }
    const data: CustomerResponse = await response.json();
    return data;
  } catch (error: any) {
    console.error("error to create user", error);
    throw error;
  }
};

