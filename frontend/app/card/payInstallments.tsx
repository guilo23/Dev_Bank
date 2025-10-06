import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button'; // Assuming you have a Button component

import { payActualBilling, getActualBilling } from '@/service/cardPayment';

export default function PayInstallment() {
  const [installments, setInstallments] = useState([]);
  const [selectedInstallment, setSelectedInstallment] = useState(null);
  const [advancePayment, setAdvancePayment] = useState(false);
  const [numToAdvance, setNumToAdvance] = useState(1);
  const [loading, setLoading] = useState(true); // State for loading status
  const [error, setError] = useState(null);ate for error messages

  useEffect(() => {
    const fetchInstallmentsData = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await getActualBilling();
        setInstallments(data);
        if (data.length > 0) {
          setSelectedInstallment(data[0].id);
        }
      } catch (err) {
        console.error('Failed to fetch installments:', err);
        setError('Could not load installments. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    fetchInstallmentsData();
  }, []);

  const handlePayment = async () => {
    if (!selectedInstallment) {
      alert('Please select an installment to pay.');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const payload = {
        installmentId: selectedInstallment,
        advance: advancePayment ? numToAdvance : 0,
      };

      await processPayment(payload);
      alert('Payment successful!');

      setInstallments((prev) => prev.filter((inst) => inst.id !== selectedInstallment));
      setSelectedInstallment(null); // Clear selection
      setAdvancePayment(false); // Reset advance payment option
    } catch (err) {
      console.error('Error processing payment:', err);
      setError(
        'An error occurred while processing your payment. Please check your details and try again.',
      );
    } finally {
      setLoading(false); // Deactivate loading state
    }
  };

  return (
    <div>
      <h1>Pay Installment / Advance Payments</h1>

      {loading && <p>Loading installments...</p>}
      {error && <p className="text-red-500">{error}</p>}

      {!loading && !error && installments.length > 0 ? (
        <div>
          <label htmlFor="installment-select" className="block text-sm font-medium text-gray-700">
            Select Installment:
          </label>
          <select
            id="installment-select"
            value={selectedInstallment || ''} // Use '' if null to prevent uncontrolled component error
            onChange={(e) => setSelectedInstallment(e.target.value)}
            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
          >
            {installments.map((inst) => (
              <option key={inst.id} value={inst.id}>
                {inst.description} - $ {inst.value.toFixed(2)} (Due: {inst.dueDate})
              </option>
            ))}
          </select>

          <div className="mt-4 flex items-center">
            <input
              type="checkbox"
              id="advance-payment"
              checked={advancePayment}
              onChange={(e) => setAdvancePayment(e.target.checked)}
              className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
            />
            <label htmlFor="advance-payment" className="ml-2 block text-sm text-gray-900">
              I want to advance payments
            </label>
          </div>

          {advancePayment && (
            <div className="mt-2">
              <label htmlFor="num-to-advance" className="block text-sm font-medium text-gray-700">
                Number of installments to advance:
              </label>
              <input
                type="number"
                id="num-to-advance"
                min="1"
                value={numToAdvance}
                onChange={(e) => setNumToAdvance(parseInt(e.target.value) || 1)} // Ensure it's at least 1
                className="mt-1 block w-20 border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm p-2"
              />
            </div>
          )}

          <Button onClick={handlePayment} className="mt-6" disabled={loading}>
            {loading ? 'Processing...' : 'Confirm Payment'}
          </Button>
        </div>
      ) : (
        !loading && !error && <p>No installments found for payment.</p>
      )}
    </div>
  );
}
