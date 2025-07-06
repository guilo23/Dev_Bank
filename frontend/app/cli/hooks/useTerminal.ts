import { useState, useEffect, useRef } from 'react';
import { TerminalLine } from '../types/terminal';
import { processCommand } from '../lib/commandProcessor';

const header = `\ _______   ________  __     __        _______    ______   __    __  __    __ 
\|       \\ |        \\|  \\   |  \\      |       \\  /      \\ |  \\  |  \\|  \\  /  \\
\| $$$$$$$\\| $$$$$$$$| $$   | $$      | $$$$$$$\\|  $$$$$$\\| $$\\ | $$| $$ /  $$ 
\| $$  | $$| $$__    | $$   | $$      | $$__/ $$| $$__| $$| $$$\\| $$| $$/  $$ 
\| $$  | $$| $$  \\    \\$$\\ /  $$      | $$    $$| $$    $$| $$$$\\ $$| $$  $$  
\| $$  | $$| $$$$$     \\$$\\  $$       | $$$$$$$\\| $$$$$$$$| $$\\$$ $$| $$$$$\\  
\| $$__/ $$| $$_____    \\$$ $$        | $$__/ $$| $$  | $$| $$ \\$$$$| $$ \\$$\\ 
\| $$    $$| $$     \\    \\$$$         | $$    $$| $$  | $$| $$  \\$$$| $$  \\$$\\
 \\$$$$$$$  \\$$$$$$$$     \\$           \\$$$$$$$  \\$$   \\$$ \\$$   \\$$ \\$$   \\$$
`;

export function useTerminal() {
  const [history, setHistory] = useState<TerminalLine[]>([
    {
      type: 'output',
      content: header,
      timestamp: new Date(),
    },
    {
      type: 'output',
      content: 'Welcome to DEV BANK!',
      timestamp: new Date(),
    },
    {
      type: 'output',
      content: 'Type "help" to see available commands.',
      timestamp: new Date(),
    },
  ]);
  const [currentInput, setCurrentInput] = useState('');
  const terminalRef = useRef<HTMLDivElement>(null);

  const executeCommand = (command: string) => {
    const trimmed = command.trim();

    setHistory((prev) => [
      ...prev,
      {
        type: 'input',
        content: `$${trimmed ? ' ' + trimmed : ''}`,
        timestamp: new Date(),
      },
    ]);

    if (!trimmed) return;

    const output: TerminalLine = processCommand(command);

    setHistory((prev) => [...prev, output]);
  };

  useEffect(() => {
    if (terminalRef.current) {
      terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
    }
  }, [history]);

  return {
    history,
    currentInput,
    setCurrentInput,
    executeCommand,
    terminalRef,
  };
}
