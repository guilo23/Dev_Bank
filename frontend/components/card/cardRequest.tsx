'use client';
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import {
  Select,
  SelectTrigger,
  SelectValue,
  SelectContent,
  SelectItem,
} from '@/components/ui/select';
import { RequestCard } from '@/service/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { useState } from 'react';

export function CardRequestDialog() {
  const [cardNumber, setCardNumber] = useState('');
  const [cardLimit, setCardLimit] = useState<number>(0);
  const [cardType, setCardType] = useState<'credit' | 'debit' | ''>('');

  const handleCardTypeChange = (value: 'credit' | 'debit') => {
    setCardType(value);
    if (value === 'credit') {
      setCardLimit(500);
    } else {
      setCardLimit(0);
    }
  };

  const handleRequest = async () => {
    try {
      const response = await RequestCard({ cardType, cardNumber, cardLimit });
      const data = response;
      console.log('card created:', data);

      setCardNumber('');
      setCardType('');
      setCardLimit(0);

      window.location.reload();
    } catch (Error) {
      console.error('Error on create card: ', Error);
    }
  };
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="default">Request card</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>New Card</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          <Input
            placeholder="Card number"
            value={cardNumber}
            onChange={(e) => setCardNumber(e.target.value)}
          />
        </div>
        <Select onValueChange={handleCardTypeChange}>
          <SelectTrigger>
            <SelectValue placeholder="Type of card" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="debit">Debit</SelectItem>
            <SelectItem value="credit">Credit</SelectItem>
          </SelectContent>
        </Select>
        <DialogFooter>
          <Button onClick={handleRequest} disabled={!cardType || !cardNumber}>
            Request Card
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
