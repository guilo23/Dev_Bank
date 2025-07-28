"use client"

import { login } from "@/service/customer"
import type React from "react"

import { useState, useEffect, useRef } from "react"

interface TerminalLine {
  type: "command" | "output" | "error" | "success" | "input"
  content: string
  timestamp?: Date
}

interface User {
  username: string
  email: string
}

interface InputState {
  mode: "normal" | "login" | "register"
  step: "username" | "password" | "email" | "confirm"
  data: { [key: string]: string }
  prompt: string
  isPassword: boolean
}

export default function Terminal() {
  const [lines, setLines] = useState<TerminalLine[]>([])
  const [currentCommand, setCurrentCommand] = useState("")
  const [commandHistory, setCommandHistory] = useState<string[]>([])
  const [historyIndex, setHistoryIndex] = useState(-1)
  const [user, setUser] = useState<User | null>(null)
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [inputState, setInputState] = useState<InputState>({
    mode: "normal",
    step: "username",
    data: {},
    prompt: "",
    isPassword: false,
  })
  const inputRef = useRef<HTMLInputElement>(null)
  const terminalRef = useRef<HTMLDivElement>(null)

  const addLine = (line: TerminalLine) => {
    setLines((prev) => [...prev, line])
  }

  const addLines = (newLines: TerminalLine[]) => {
    setLines((prev) => [...prev, ...newLines])
  }

  const startLogin = () => {
    setInputState({
      mode: "login",
      step: "email",
      data: {},
      prompt: "Email: ",
      isPassword: false,
    })
  }

  const startRegister = () => {
    setInputState({
      mode: "register",
      step: "username",
      data: {},
      prompt: "Username: ",
      isPassword: false,
    })
  }

  const processLoginStep = async (input: string) => {
    const { step, data } = inputState
    const newData = { ...data, password: input }

    if (step === "email") {
      setInputState({
        mode: "login",
        step: "password",
        data: newData,
        prompt: "Password: ",
        isPassword: true,
      })
      addLine({ type: "input", content: `Email: ${input}` })
    } else if (step === "password") {
      addLine({ type: "input", content: `Password: ${"*".repeat(input.length)}` })

      try {
        const result = await login({
          email: newData.email,
          password: newData.password,
        })

        setUser({ username: 'user', email: newData.email})
        setIsLoggedIn(true)
        addLine({ type: "success", content: `Login realizado com sucesso! Bem-vindo, ${newData.email}!` })
      } catch {
        addLine({ type: "error", content: "Credenciais inválidas!" })
      }

      setInputState({
        mode: "normal",
        step: "username",
        data: {},
        prompt: "",
        isPassword: false,
      })
    }
  }

  const processRegisterStep = (input: string) => {
    const { step, data } = inputState
    const newData = { ...data, username: input }

    if (step === "username") {
      setInputState({
        mode: "register",
        step: "email",
        data: newData,
        prompt: "Email: ",
        isPassword: false,
      })
      addLine({ type: "input", content: `Username: ${input}` })
    } else if (step === "email") {
      setInputState({
        mode: "register",
        step: "password",
        data: newData,
        prompt: "Password: ",
        isPassword: true,
      })
      addLine({ type: "input", content: `Email: ${input}` })
    } else if (step === "password") {
      setInputState({
        mode: "register",
        step: "confirm",
        data: newData,
        prompt: "Confirm Password: ",
        isPassword: true,
      })
      addLine({ type: "input", content: `Password: ${"*".repeat(input.length)}` })
    } else if (step === "confirm") {
      addLine({ type: "input", content: `Confirm Password: ${"*".repeat(input.length)}` })

      if (input === data.password) {
        setUser({ username: data.username, email: data.email })
        setIsLoggedIn(true)
        addLines([
          { type: "success", content: "Conta criada com sucesso!" },
          { type: "success", content: `Bem-vindo, ${data.username}!` },
        ])
      } else {
        addLine({ type: "error", content: "Senhas não coincidem!" })
      }

      setInputState({
        mode: "normal",
        step: "username",
        data: {},
        prompt: "",
        isPassword: false,
      })
    }
  }

  const commands = {
    help: () => {
      return [
        { type: "output" as const, content: "Comandos disponíveis:" },
        { type: "output" as const, content: "  help          - Mostra esta mensagem de ajuda" },
        { type: "output" as const, content: "  clear         - Limpa o terminal" },
        { type: "output" as const, content: "  login         - Fazer login no sistema" },
        { type: "output" as const, content: "  register      - Registrar nova conta" },
        { type: "output" as const, content: "  logout        - Fazer logout" },
        { type: "output" as const, content: "  whoami        - Mostra usuário atual" },
        { type: "output" as const, content: "  status        - Status do sistema" },
        { type: "output" as const, content: "  version       - Versão do sistema" },
        { type: "output" as const, content: "  date          - Data e hora atual" },
        { type: "output" as const, content: "  echo <texto>  - Repete o texto" },
      ]
    },
    clear: () => {
      setLines([])
      return []
    },
    login: () => {
      if (isLoggedIn) {
        return [{ type: "error" as const, content: "Você já está logado!" }]
      }
      startLogin()
      return []
    },
    register: () => {
      if (isLoggedIn) {
        return [{ type: "error" as const, content: "Você já está logado!" }]
      }
      startRegister()
      return []
    },
    logout: () => {
      if (!isLoggedIn) {
        return [{ type: "error" as const, content: "Você não está logado!" }]
      }
      setUser(null)
      setIsLoggedIn(false)
      return [{ type: "success" as const, content: "Logout realizado com sucesso!" }]
    },
    whoami: () => {
      if (!isLoggedIn) {
        return [{ type: "output" as const, content: "Usuário não autenticado" }]
      }
      return [
        { type: "output" as const, content: `Username: ${user?.username}` },
        { type: "output" as const, content: `Email: ${user?.email}` },
      ]
    },
    status: () => {
      return [
        { type: "output" as const, content: `Status: ${isLoggedIn ? "Autenticado" : "Não autenticado"}` },
        { type: "output" as const, content: `Sessão: ${isLoggedIn ? "Ativa" : "Inativa"}` },
        { type: "output" as const, content: `Sistema: Online` },
        {
          type: "output" as const,
          content: `Uptime: ${Math.floor(Math.random() * 100)}h ${Math.floor(Math.random() * 60)}m`,
        },
      ]
    },
    version: () => {
      return [
        { type: "output" as const, content: "Terminal CLI v1.0.0" },
        { type: "output" as const, content: "Built with React & Next.js" },
      ]
    },
    date: () => {
      return [{ type: "output" as const, content: new Date().toLocaleString("pt-BR") }]
    },
    echo: (args: string[]) => {
      return [{ type: "output" as const, content: args.join(" ") }]
    },
  }

  const executeCommand = (command: string) => {
    const trimmedCommand = command.trim()
    if (!trimmedCommand) return

    if (inputState.mode !== "normal") {
      if (inputState.mode === "login") {
        processLoginStep(trimmedCommand)
      } else if (inputState.mode === "register") {
        processRegisterStep(trimmedCommand)
      }
      setCurrentCommand("")
      return
    }

    setCommandHistory((prev) => [...prev, trimmedCommand])
    setHistoryIndex(-1)

    const commandLine: TerminalLine = {
      type: "command",
      content: `${getPrompt()}${trimmedCommand}`,
      timestamp: new Date(),
    }

    const [cmd, ...args] = trimmedCommand.split(" ")

    let output: TerminalLine[] = []

    if (cmd in commands) {
      const result = (commands as any)[cmd](args)
      output = Array.isArray(result) ? result : [result]
    } else {
      output = [
        {
          type: "error" as const,
          content: `Comando não encontrado: ${cmd}. Digite 'help' para ver os comandos disponíveis.`,
        },
      ]
    }

    setLines((prev) => [...prev, commandLine, ...output])
    setCurrentCommand("")
  }

  const getPrompt = () => {
    if (inputState.mode !== "normal") {
      return inputState.prompt
    }
    const username = isLoggedIn ? user?.username : "guest"
    return `${username}@terminal:~$ `
  }

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") {
      executeCommand(currentCommand)
    } else if (e.key === "ArrowUp" && inputState.mode === "normal") {
      e.preventDefault()
      if (commandHistory.length > 0) {
        const newIndex = historyIndex === -1 ? commandHistory.length - 1 : Math.max(0, historyIndex - 1)
        setHistoryIndex(newIndex)
        setCurrentCommand(commandHistory[newIndex])
      }
    } else if (e.key === "ArrowDown" && inputState.mode === "normal") {
      e.preventDefault()
      if (historyIndex !== -1) {
        const newIndex = historyIndex + 1
        if (newIndex >= commandHistory.length) {
          setHistoryIndex(-1)
          setCurrentCommand("")
        } else {
          setHistoryIndex(newIndex)
          setCurrentCommand(commandHistory[newIndex])
        }
      }
    } else if (e.key === "Escape" && inputState.mode !== "normal") {
      setInputState({
        mode: "normal",
        step: "username",
        data: {},
        prompt: "",
        isPassword: false,
      })
      addLine({ type: "error", content: "Operação cancelada." })
      setCurrentCommand("")
    }
  }

  useEffect(() => {
    if (terminalRef.current) {
      terminalRef.current.scrollTop = terminalRef.current.scrollHeight
    }
  }, [lines])

  useEffect(() => {
    if (inputRef.current) {
      inputRef.current.focus()
    }

    // setLines([
    //   { type: "output", content: "╔══════════════════════════════════════════════════════════════╗" },
    //   { type: "output", content: "║                    Terminal CLI Interface                    ║" },
    //   { type: "output", content: "║                        Bem-vindo!                           ║" },
    //   { type: "output", content: "╚══════════════════════════════════════════════════════════════╝" },
    //   { type: "output", content: "" },
    //   { type: "output", content: 'Digite "help" para ver os comandos disponíveis.' },
    //   { type: "output", content: 'Use "login" para entrar (admin/admin) ou "register" para criar conta.' },
    //   { type: "output", content: "" },
    // ])
  }, [])

  const handleTerminalClick = () => {
    if (inputRef.current) {
      inputRef.current.focus()
    }
  }

  return (
    <div className="h-screen w-full bg-black text-green-400 font-mono text-sm overflow-hidden flex flex-col">
      {/* Header */}
      <div className="bg-gray-900 border-b border-gray-700 p-2 flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <div className="w-3 h-3 bg-red-500 rounded-full"></div>
          <div className="w-3 h-3 bg-yellow-500 rounded-full"></div>
          <div className="w-3 h-3 bg-green-500 rounded-full"></div>
          {/* <span className="ml-4 text-gray-300">Terminal CLI</span> */}
        </div>
        <div className="text-gray-400 text-xs">
          {isLoggedIn ? `Logado como: ${user?.username}` : "Não autenticado"}
          {inputState.mode !== "normal" && (
            <span className="ml-4 text-yellow-400">
              {inputState.mode === "login" ? "LOGIN" : "REGISTRO"} - ESC para cancelar
            </span>
          )}
        </div>
      </div>

      {/* Terminal Content */}
      <div ref={terminalRef} className="flex-1 p-4 overflow-y-auto cursor-text" onClick={handleTerminalClick}>
        {lines.map((line, index) => (
          <div
            key={index}
            className={`mb-1 ${line.type === "command"
                ? "text-white"
                : line.type === "error"
                  ? "text-red-400"
                  : line.type === "success"
                    ? "text-green-400"
                    : line.type === "input"
                      ? "text-cyan-400"
                      : "text-gray-300"
              }`}
          >
            {line.content}
          </div>
        ))}

        {/* Input Line */}
        <div className="flex items-center relative">
          <span className={`mr-2 ${inputState.mode !== "normal" ? "text-yellow-400" : "text-green-400"}`}>
            {getPrompt()}
          </span>
          <div className="flex-1 relative">
            <input
              ref={inputRef}
              type="text"
              value={currentCommand}
              onChange={(e) => setCurrentCommand(e.target.value)}
              onKeyDown={handleKeyDown}
              className="w-full bg-transparent border-none outline-none text-white caret-green-400"
              style={inputState.isPassword ? { color: "transparent" } : {}}
              autoFocus
            />
            {inputState.isPassword && (
              <div className="absolute inset-0 pointer-events-none text-white">{"*".repeat(currentCommand.length)}</div>
            )}
          </div>
        </div>
      </div>

      {/* Status Bar */}
      <div className="bg-gray-900 border-t border-gray-700 p-2 text-xs text-gray-400 flex justify-between">
        <div>
          {inputState.mode === "normal"
            ? "Pressione ↑/↓ para navegar no histórico | Enter para executar"
            : "Digite a informação solicitada | ESC para cancelar"}
        </div>
        <div>{new Date().toLocaleTimeString("pt-BR")}</div>
      </div>
    </div>
  )
}
