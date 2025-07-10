'use client';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Switch } from '@/components/ui/switch';
import { Label } from '@/components/ui/label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { CreditCard, Eye, EyeOff, Lock, Unlock, Plus, Settings } from 'lucide-react';
import { useState } from 'react';

export default function CardManagement() {
  const [showCardNumbers, setShowCardNumbers] = useState(false);
  const [cardLocked, setCardLocked] = useState(false);

  const debitCards = [
    {
      id: 1,
      type: 'Debit',
      number: '1234 5678 9012 3456',
      holder: 'JOAO DA SILVA',
      expiry: '12/26',
      status: 'active',
      limit: 5000,
    },
  ];

  const creditCards = [
    {
      id: 1,
      type: 'Credit',
      number: '9876 5432 1098 7654',
      holder: 'JOAO DA SILVA',
      expiry: '08/27',
      status: 'active',
      limit: 15000,
      used: 3250,
      available: 11750,
    },
  ];

  const formatCardNumber = (number: string) => {
    if (!showCardNumbers) {
      return '**** **** **** ' + number.slice(-4);
    }
    return number;
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Card Management</h1>
        <Button>
          <Plus className="h-4 w-4 mr-2" />
          Request Card
        </Button>
      </div>

      <Tabs defaultValue="debit" className="w-full">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="debit">Debit Cards</TabsTrigger>
          <TabsTrigger value="credit">Credit Cards</TabsTrigger>
        </TabsList>

        <TabsContent value="debit" className="space-y-6">
          <div className="grid gap-6 md:grid-cols-2">
            {debitCards.map((card) => (
              <Card key={card.id} className="relative overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-blue-600 to-purple-700 opacity-10" />
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <CardTitle className="flex items-center gap-2">
                      <CreditCard className="h-5 w-5" />
                      {card.type} Card
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
                    <p className="font-mono text-lg">{formatCardNumber(card.number)}</p>
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-muted-foreground">Card Holder</p>
                      <p className="font-medium">{card.holder}</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Expiry</p>
                      <p className="font-medium">{card.expiry}</p>
                    </div>
                  </div>

                  <div>
                    <p className="text-sm text-muted-foreground">Daily Limit</p>
                    <p className="font-medium">R$ {card.limit.toLocaleString()}</p>
                  </div>

                  <div className="flex items-center justify-between pt-4 border-t">
                    <div className="flex items-center space-x-2">
                      <Switch
                        id="card-lock"
                        checked={!cardLocked}
                        onCheckedChange={(checked) => setCardLocked(!checked)}
                      />
                      <Label htmlFor="card-lock" className="flex items-center gap-2">
                        {cardLocked ? <Lock className="h-4 w-4" /> : <Unlock className="h-4 w-4" />}
                        {cardLocked ? 'Locked' : 'Unlocked'}
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

        <TabsContent value="credit" className="space-y-6">
          <div className="grid gap-6 md:grid-cols-2">
            {creditCards.map((card) => (
              <Card key={card.id} className="relative overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-green-600 to-blue-700 opacity-10" />
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <CardTitle className="flex items-center gap-2">
                      <CreditCard className="h-5 w-5" />
                      {card.type} Card
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
                    <p className="font-mono text-lg">{formatCardNumber(card.number)}</p>
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-muted-foreground">Card Holder</p>
                      <p className="font-medium">{card.holder}</p>
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
                        R$ {card.used.toLocaleString()} / R$ {card.limit.toLocaleString()}
                      </span>
                    </div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className="bg-primary h-2 rounded-full"
                        style={{ width: `${(card.used / card.limit) * 100}%` }}
                      />
                    </div>
                  </div>

                  <div>
                    <p className="text-sm text-muted-foreground">Available Limit</p>
                    <p className="font-medium text-green-600">
                      R$ {card.available.toLocaleString()}
                    </p>
                  </div>

                  <div className="flex items-center justify-between pt-4 border-t">
                    <div className="flex items-center space-x-2">
                      <Switch
                        id="credit-card-lock"
                        checked={!cardLocked}
                        onCheckedChange={(checked) => setCardLocked(!checked)}
                      />
                      <Label htmlFor="credit-card-lock" className="flex items-center gap-2">
                        {cardLocked ? <Lock className="h-4 w-4" /> : <Unlock className="h-4 w-4" />}
                        {cardLocked ? 'Locked' : 'Unlocked'}
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

          {/* Credit Card Actions */}
          <Card>
            <CardHeader>
              <CardTitle>Credit Card Actions</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-3">
                <Button variant="outline" className="h-20 flex-col gap-2">
                  <CreditCard className="h-6 w-6" />
                  Increase Limit
                </Button>
                <Button variant="outline" className="h-20 flex-col gap-2">
                  <Settings className="h-6 w-6" />
                  Installment Options
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
