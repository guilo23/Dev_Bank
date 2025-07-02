"use client";

import { useEffect, useState } from "react";
import { Card, CardHeader, CardTitle } from "@/components/ui/card";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Button } from "@/components/ui/button";
import { CreditCard, TrendingUp, Banknote } from "lucide-react";
import { getAccounts } from "@/service/account";
import { accountResponse } from "@/types/account";
import { setCookie } from "nookies";
import { useRouter } from "next/navigation";

export default function AccountSelection() {
  const [accounts, setAccounts] = useState<accountResponse[]>([]);
  const [selectedId, setSelectedId] = useState<string>("");
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        const response = await getAccounts();
        setAccounts(response);
      } catch (error) {
        console.error("Failed to fetch accounts", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAccounts();
  }, []);

  const handleConfirm = () => {
    if (selectedId) {
      setCookie(null, "accountNumber", selectedId, {
        path: "/",
        maxAge: 60 * 60,
        sameSite: "lax",
      });
      router.push("/");
    }
  };

  const renderIcon = (type: string) => {
    switch (type) {
      case "CHECKING":
        return <Banknote className="h-5 w-5 mr-2 text-blue-600" />;
      case "SAVINGS":
        return <TrendingUp className="h-5 w-5 mr-2 text-green-600" />;
      case "CREDIT":
        return <CreditCard className="h-5 w-5 mr-2 text-purple-600" />;
      default:
        return null;
    }
  };

  if (loading) return <p>Loading accounts...</p>;

  return (
    <div className="max-w-2xl mx-auto p-6 space-y-6">
      <h1 className="text-2xl font-bold">Select an Account</h1>
      <RadioGroup
        value={selectedId}
        onValueChange={(val) => setSelectedId(val)}
      >
        {" "}
        {accounts.map((account) => (
          <Card key={account.accountNumber} className="mb-2">
            <CardHeader className="flex items-center">
              <div className="flex items-center">
                <RadioGroupItem
                  value={account.accountNumber.toString()}
                  className="mr-4"
                />
                {renderIcon(account.customerName)}
                <CardTitle className="text-base font-medium">
                  {account.customerName} - {account.accountType} - R${" "}
                  {account.currentBalance.toFixed(2)}
                </CardTitle>{" "}
              </div>
            </CardHeader>
          </Card>
        ))}
      </RadioGroup>
      <Button disabled={!selectedId} onClick={handleConfirm}>
        Continue
      </Button>
    </div>
  );
}
