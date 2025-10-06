'use client';
import { CardRequestDialog } from '@/components/card/cardRequest';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Switch } from '@/components/ui/switch';
import { Label } from '@/components/ui/label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { CreditCard, Eye, EyeOff, Lock, Unlock, Settings } from 'lucide-react';
import { useEffect, useState } from 'react';
import { getCards } from '@/service/card';
import { cardResponse } from '@/types/card';

// Extendendo os dados sem alterar cardResponse
type ExtendedCard = cardResponse & {
  status: 'active' | 'inactive';
  locked: boolean;
  expiry: string;
  available?: number;
};

export default function CardManagement() {
  const [showCardNumbers, setShowCardNumbers] = useState(false);
  const [cards, setCards] = useState<ExtendedCard[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const rawCards: cardResponse[] = await getCards();

        const enriched: ExtendedCard[] = rawCards.map((card) => {
          const isCredit = card.cardType === 'CREDIT';
          const used = isCredit ? Math.floor(Math.random() * card.cardLimit) : undefined;
          const createdDate = new Date();
          const expiryYear = (createdDate.getFullYear() + 5).toString().slice(-2);

          return {
            ...card,
            status: Math.random() > 0.2 ? 'active' : 'inactive',
            locked: false,
            expiry: `${String(Math.floor(Math.random() * 12 + 1)).padStart(2, '0')}/${expiryYear}`,
            used,
            available:
              isCredit && card.cardBilling !== undefined
                ? card.cardLimit - card.cardBilling
                : undefined,
          };
        });

        setCards(enriched);
      } catch (error) {
        console.error('Failed to fetch cards', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const toggleLock = (id: string) => {
    setCards((prev) =>
      prev.map((card) => (card.cardId === id ? { ...card, locked: !card.locked } : card)),
    );
  };

  const formatCardNumber = (number: string) =>
    showCardNumbers ? number : '**** **** **** ' + number.slice(-4);

  const debitCards = cards.filter((card) => card.cardType === 'DEBIT');
  const creditCards = cards.filter((card) => card.cardType === 'CREDIT');

  if (loading) return <p className="text-center">Loading cards...</p>;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Card Management</h1>
        <CardRequestDialog />
      </div>

      <Tabs defaultValue="debit" className="w-full">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="debit">Debit Cards</TabsTrigger>
          <TabsTrigger value="credit">Credit Cards</TabsTrigger>
        </TabsList>

        {/* DEBIT CARDS */}
        <TabsContent value="debit" className="space-y-6">
          <div className="grid gap-6 md:grid-cols-2">
            {debitCards.map((card) => (
              <Card key={card.cardId} className="relative overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-blue-600 to-purple-700 opacity-10" />
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <CardTitle className="flex items-center gap-2">
                      <CreditCard className="h-5 w-5" />
                      Debit Card
                    </CardTitle>
                    <Badge variant={card.status === 'active' ? 'default' : 'secondary'}>
                      {card.status === 'active' ? 'Active' : 'Inactive'}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-muted-foreground">Card Number</span>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => setShowCardNumbers(!showCardNumbers)}
                      >
                        {showCardNumbers ? (
                          <EyeOff className="h-4 w-4" />
                        ) : (
                          <Eye className="h-4 w-4" />
                        )}
                      </Button>
                    </div>
                    <p className="font-mono text-lg">{formatCardNumber(card.cardNumber)}</p>
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-muted-foreground">Card Holder</p>
                      <p className="font-medium">{card.customername}</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Expiry</p>
                      <p className="font-medium">{card.expiry}</p>
                    </div>
                  </div>

                  <div>
                    <p className="text-sm text-muted-foreground">Daily Limit</p>
                    <p className="font-medium">R$ {card.cardLimit.toLocaleString()}</p>
                  </div>

                  <div className="flex items-center justify-between pt-4 border-t">
                    <div className="flex items-center space-x-2">
                      <Switch
                        id={`debit-lock-${card.cardId}`}
                        checked={!card.locked}
                        onCheckedChange={() => toggleLock(card.cardId)}
                      />
                      <Label
                        htmlFor={`debit-lock-${card.cardId}`}
                        className="flex items-center gap-2"
                      >
                        {card.locked ? (
                          <Lock className="h-4 w-4" />
                        ) : (
                          <Unlock className="h-4 w-4" />
                        )}
                        {card.locked ? 'Locked' : 'Unlocked'}
                      </Label>
                    </div>
                    <Button variant="outline" size="sm">
                      <Settings className="h-4 w-4 mr-2" />
                      Settings
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* CREDIT CARDS */}
        <TabsContent value="credit" className="space-y-6">
          <div className="grid gap-6 md:grid-cols-2">
            {creditCards.map((card) => (
              <Card key={card.cardId} className="relative overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-green-600 to-blue-700 opacity-10" />
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <CardTitle className="flex items-center gap-2">
                      <CreditCard className="h-5 w-5" />
                      Credit Card
                    </CardTitle>
                    <Badge variant={card.status === 'active' ? 'default' : 'secondary'}>
                      {card.status === 'active' ? 'Active' : 'Inactive'}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-muted-foreground">Card Number</span>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => setShowCardNumbers(!showCardNumbers)}
                      >
                        {showCardNumbers ? (
                          <EyeOff className="h-4 w-4" />
                        ) : (
                          <Eye className="h-4 w-4" />
                        )}
                      </Button>
                    </div>
                    <p className="font-mono text-lg">{formatCardNumber(card.cardNumber)}</p>
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-muted-foreground">Card Holder</p>
                      <p className="font-medium">{card.customername}</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Expiry</p>
                      <p className="font-medium">{card.expiry}</p>
                    </div>
                  </div>

                  <div className="space-y-2">
                    <div className="flex justify-between">
                      <span className="text-sm text-muted-foreground">Used Limit</span>
                      <span className="text-sm font-medium">
                        R$ {card.cardBilling?.toLocaleString() ?? 0} / R${' '}
                        {card.cardLimit.toLocaleString()}
                      </span>
                    </div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className="bg-primary h-2 rounded-full"
                        style={{ width: `${((card.cardBilling ?? 0) / card.cardLimit) * 100}%` }}
                      />
                    </div>
                  </div>

                  <div>
                    <p className="text-sm text-muted-foreground">Available Limit</p>
                    <p className="font-medium text-green-600">
                      R$ {card.available?.toLocaleString() ?? 0}
                    </p>
                  </div>

                  <div className="flex items-center justify-between pt-4 border-t">
                    <div className="flex items-center space-x-2">
                      <Switch
                        id={`credit-lock-${card.cardId}`}
                        checked={!card.locked}
                        onCheckedChange={() => toggleLock(card.cardId)}
                      />
                      <Label
                        htmlFor={`credit-lock-${card.cardId}`}
                        className="flex items-center gap-2"
                      >
                        {card.locked ? (
                          <Lock className="h-4 w-4" />
                        ) : (
                          <Unlock className="h-4 w-4" />
                        )}
                        {card.locked ? 'Locked' : 'Unlocked'}
                      </Label>
                    </div>
                    <Button variant="outline" size="sm">
                      <Settings className="h-4 w-4 mr-2" />
                      Settings
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
          <Card>
            <CardHeader>
              <CardTitle>Credit Card Actions</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-3">
                <Button variant="outline" className="h-20 flex-col gap-2">
                  <CreditCard className="h-6 w-6" />
                  Request Increase Limit
                </Button>
                <Button variant="outline" className="h-20 flex-col gap-2">
                  <Settings className="h-6 w-6" />
                  pay Installment
                </Button>
                <Button variant="outline" className="h-20 flex-col gap-2">
                  <Eye className="h-6 w-6" />
                  View Bill
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
