import { TerminalLine } from '../types/terminal';

export default function executeHelpCommand(): TerminalLine {
  const availableCommands = [{ name: 'help', description: 'Display this help message xD' }];
  const content = availableCommands.map((cmd) => ` ${cmd.name} - ${cmd.description}`).join(`\n`);

  return {
    type: 'output',
    content: content,
    timestamp: new Date(),
  };
}
