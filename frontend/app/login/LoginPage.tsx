"use client";

import type React from "react";
import { useState } from "react";

const LoginPageComponente: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    //console.log("Login attempt", { email, password })
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Login</h2>
        <p style={styles.description}>
          Entre com suas credenciais para acessar sua conta
        </p>
        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.inputGroup}>
            <label htmlFor="email" style={styles.label}>
              Email
            </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="seu@email.com"
              style={styles.input}
              required
            />
          </div>
          <div style={styles.inputGroup}>
            <label htmlFor="password" style={styles.label}>
              Senha
            </label>
            <div style={styles.passwordContainer}>
              <input
                type={showPassword ? "text" : "password"}
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={styles.input}
                required
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                style={styles.togglePassword}
              >
                {showPassword ? "Ocultar" : "Mostrar"}
              </button>
            </div>
          </div>
          <button type="submit" style={styles.button}>
            Entrar
          </button>
        </form>
        <a href="#" style={styles.forgotPassword}>
          Esqueceu sua senha?
        </a>
      </div>
    </div>
  );
};

const styles = {
  container: {
    minHeight: "100vh",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#000",
    padding: "20px",
  },
  card: {
    backgroundColor: "#1a1a1a",
    borderRadius: "8px",
    padding: "40px",
    width: "100%",
    maxWidth: "400px",
    boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
  },
  title: {
    color: "#fff",
    fontSize: "24px",
    textAlign: "center" as const,
    marginBottom: "10px",
  },
  description: {
    color: "#a0a0a0",
    textAlign: "center" as const,
    marginBottom: "20px",
  },
  form: {
    display: "flex",
    flexDirection: "column" as const,
  },
  inputGroup: {
    marginBottom: "20px",
  },
  label: {
    color: "#fff",
    marginBottom: "5px",
    display: "block",
  },
  input: {
    width: "100%",
    padding: "10px",
    borderRadius: "4px",
    border: "1px solid #333",
    backgroundColor: "#2a2a2a",
    color: "#fff",
    fontSize: "16px",
  },
  passwordContainer: {
    position: "relative" as const,
  },
  togglePassword: {
    position: "absolute" as const,
    right: "10px",
    top: "50%",
    transform: "translateY(-50%)",
    background: "none",
    border: "none",
    color: "#a0a0a0",
    cursor: "pointer",
  },
  button: {
    width: "100%",
    padding: "10px",
    borderRadius: "4px",
    border: "none",
    backgroundColor: "#fff",
    color: "#000",
    fontSize: "16px",
    cursor: "pointer",
    marginTop: "10px",
  },
  forgotPassword: {
    color: "#a0a0a0",
    textAlign: "center" as const,
    display: "block",
    marginTop: "20px",
    textDecoration: "none",
  },
};

export default LoginPageComponente;
