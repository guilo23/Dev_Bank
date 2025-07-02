'use client';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { ArrowUpRight, ArrowDownLeft, CreditCard, TrendingUp, Eye, EyeOff } from 'lucide-react';
import { useState, useEffect } from 'react';
import { getAccountByNumber } from '@/service/account';
import { accountResponse } from '@/types/account';

export default function AccountOverviewComponent() {
  const [showBalance, setShowBalance] = useState(true);
  const [account, setAccount] = useState<accountResponse>();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAccount = async () => {
      try {
        const response = await getAccountByNumber();
        setAccount(response);
      } catch (error) {
        console.error('Failed to fetch accounts', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAccount();
  });

  const recentTransactions = [
    {
      id: 1,
      description: 'PIX Transfer - Joaoo Silva',
      amount: -250.0,
      date: '2024-01-15',
      type: 'transfer',
    },
    {
      id: 2,
      description: 'Salary - Company XYZ',
      amount: 5500.0,
      date: '2024-01-14',
      type: 'income',
    },
    {
      id: 3,
      description: 'Purchase - ABC Supermarket',
      amount: -89.5,
      date: '2024-01-13',
      type: 'purchase',
    },
    {
      id: 4,
      description: 'Received Transfer',
      amount: 150.0,
      date: '2024-01-12',
      type: 'income',
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Account Overview</h1>
        <Button variant="outline" size="sm" onClick={() => setShowBalance(!showBalance)}>
          {showBalance ? <EyeOff className="h-4 w-4 mr-2" /> : <Eye className="h-4 w-4 mr-2" />}
          {showBalance ? 'Hide' : 'Show'} Balance
        </Button>
      </div>

      {/* Account Balance Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Current Balance</CardTitle>
            <CreditCard className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {showBalance ? account?.currentBalance : 'R$ ******'}
            </div>
            <p className="text-xs text-muted-foreground">Checking Account</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Credit Card</CardTitle>
            <CreditCard className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{showBalance ? 'R$ 1,234.56' : 'R$ ******'}</div>
            <p className="text-xs text-muted-foreground">Limit: R$ 5,000.00</p>
          </CardContent>
        </Card>
      </div>

      {/* Quick Actions */}
      <Card>
        <CardHeader>
          <CardTitle>Quick Actions</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            <Button className="h-20 flex-col gap-2">
              <ArrowUpRight className="h-6 w-6" />
              Transfer
            </Button>
            <Button variant="outline" className="h-20 flex-col gap-2">
              <ArrowDownLeft className="h-6 w-6" />
              Loan
            </Button>
            <Button variant="outline" className="h-20 flex-col gap-2">
              <CreditCard className="h-6 w-6" />
              Cards
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Recent Transactions */}
      <Card>
        <CardHeader>
          <CardTitle>Recent Transactions</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {recentTransactions.map((transaction) => (
              <div key={transaction.id} className="flex items-center justify-between">
                <div className="flex items-center space-x-4">
                  <div
                    className={`p-2 rounded-full ${transaction.type === 'income' ? 'bg-green-100' : 'bg-red-100'}`}
                  >
                    {transaction.type === 'income' ? (
                      <ArrowDownLeft className="h-4 w-4 text-green-600" />
                    ) : (
                      <ArrowUpRight className="h-4 w-4 text-red-600" />
                    )}
                  </div>
                  <div>
                    <p className="font-medium">{transaction.description}</p>
                    <p className="text-sm text-muted-foreground">{transaction.date}</p>
                  </div>
                </div>
                <div
                  className={`font-bold ${transaction.amount > 0 ? 'text-green-600' : 'text-red-600'}`}
                >
                  {transaction.amount > 0 ? '+' : ''}R$ {Math.abs(transaction.amount).toFixed(2)}
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
