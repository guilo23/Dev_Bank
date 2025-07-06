import executeHelpCommand from '../commands/help';
import { TerminalLine } from '../types/terminal';

export function processCommand(command: string): TerminalLine {
  const trimmedCommand = command.trim().toLowerCase();

  switch (trimmedCommand) {
    case 'help':
      return executeHelpCommand();
    case '':
      return {
        type: 'input',
        content: '',
      };
    default:
      return {
        type: 'error',
        content: `Command not found: ${trimmedCommand}. Type "help" for available commands.`,
      };
  }
}
