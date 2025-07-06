'use client';

import type React from 'react';
import { useRef } from 'react';
import { useTerminal } from '../hooks/useTerminal';

export default function Terminal() {
  const { history, currentInput, setCurrentInput, executeCommand, terminalRef } = useTerminal();
  const inputRef = useRef<HTMLInputElement>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    executeCommand(currentInput);
    setCurrentInput('');
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.target !== inputRef.current && !e.ctrlKey && !e.altKey && !e.metaKey) {
      inputRef.current?.focus();
    }
  };

  const handleTerminalClick = () => {
    inputRef.current?.focus();
  };

  return (
    <div
      className="h-screen w-full bg-black text-green-400 font-mono text-sm overflow-hidden flex flex-col"
      onClick={handleTerminalClick}
      onKeyDown={handleKeyDown}
      tabIndex={-1}
    >
      <div ref={terminalRef} className="flex-1 overflow-y-auto p-4 space-y-1">
        {history.map((line, index) => (
          <div key={index} className="flex">
            <span
              className={`whitespace-pre-wrap break-words ${
                line.type === 'input' && line.content !== '$'
                  ? 'text-white'
                  : line.type === 'error'
                    ? 'text-red-400'
                    : 'text-green-400'
              }`}
            >
              {line.content}
            </span>
          </div>
        ))}

        <div className="flex items-center">
          <span className="text-green-400 mr-2">$</span>
          <form onSubmit={handleSubmit} className="flex-1">
            <input
              ref={inputRef}
              type="text"
              value={currentInput}
              onChange={(e) => setCurrentInput(e.target.value)}
              className="bg-transparent border-none outline-none text-white font-mono text-sm w-full"
              placeholder=""
              autoComplete="off"
              spellCheck="false"
            />
          </form>
          <span className="text-green-400 animate-pulse">█</span>
        </div>
      </div>
    </div>
  );
}
