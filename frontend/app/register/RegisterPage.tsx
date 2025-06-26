"use client";

import type React from "react";
import { login } from "@/service/customer";
import { useRouter } from "next/navigation";
import { useState } from "react";

const RegisterPageComponente: React.FC = () => {
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [showPassword, setShowPassword] = useState(false);
	const [birthday, setBirthday] = useState("");
	const [cpf, setCpf] = useState("");
	const [name,setName] = useState("");
	const [phoneNumber, setPhoneNumber] = useState("");
	const [accountType, setAccountType] = useState("");
	const router = useRouter();

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();
		try {
			const data = await login({ email, password });
			localStorage.setItem("token", data.token);
			//router.push("home");
			console.log(data.token);
		} catch (error) {
			alert("email or password is wrong");
		}
	};
	return (
		<div style={styles.container}>
			<div style={styles.card}>
				<h2 style={styles.title}>Register</h2>
				<p style={styles.description}>
					Entre com suas credenciais para acessar sua conta
				</p>
				<form onSubmit={handleSubmit} style={styles.form}>
			<div style={styles.inputGroup}>
						<label htmlFor="name" style={styles.label}>
							name
						</label>
						<input
							type="name"
							id="name"
							value={name}
							onChange={(e) => setName(e.target.value)}
							placeholder="your name"
							style={styles.input}
							required
						/>
					</div>
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
							password
						</label>
						<div style={styles.passwordContainer}>
							<input
								type={showPassword ? "text" : "password"}
								id="password"
								value={password}
								onChange={(e) => setPassword(e.target.value)}
								style={styles.passwordInput}
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
					<div style={styles.inputGroup}>
						<label htmlFor="password" style={styles.label}>
						confirm	password
						</label>
						<div style={styles.passwordContainer}>
							<input
								type={showPassword ? "text" : "password"}
								id="password"
								value={password}
								onChange={(e) => setPassword(e.target.value)}
								style={styles.passwordInput}
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
					<div style={styles.inputGroup}>
						<label htmlFor="birthday" style={styles.label}>
							birthday
						</label>
							<input
								type="birthday"
								id="birthday"
								value={birthday}
								onChange={(e) => setBirthday(e.target.value)}
								style={styles.input}
								required
							/>
						</div>
					<div style={styles.inputGroup}>
						<label htmlFor="cpf" style={styles.label}>
							cpf
						</label>
							<input
								type="cpf"
								id="cpf"
								value={cpf}
								onChange={(e) => setCpf(e.target.value)}
								style={styles.input}
								required
							/>
					</div>
					<div style={styles.inputGroup}>
						<label htmlFor="phoneNumber" style={styles.label}>
							phoneNumber
						</label>
							<input
								type="phoneNumber"
								id="phoneNumber"
								value={phoneNumber}
								onChange={(e) => setPhoneNumber(e.target.value)}
								style={styles.input}
								required
							/>
					</div>
					<div style={styles.inputGroup}>
						<label htmlFor="accountType" style={styles.label}>
							accountType
						</label>
							<input
								type="accountType"
								id="accountType"
								value={accountType}
								onChange={(e) => setAccountType(e.target.value)}
								style={styles.input}
								required
							/>
					</div>
					<button type="submit" style={styles.button}>
						Entrar
					</button>
				</form>
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
    borderRadius: "10px",
    padding: "40px 30px",
    width: "100%",
    maxWidth: "460px",
    boxShadow: "0 4px 12px rgba(0, 0, 0, 0.3)",
  },
  title: {
    color: "#ffffff",
    fontSize: "26px",
    fontWeight: "bold" as const,
    textAlign: "center" as const,
    marginBottom: "10px",
  },
  description: {
    color: "#c0c0c0",
    fontSize: "15px",
    textAlign: "center" as const,
    marginBottom: "30px",
  },
  form: {
    display: "flex",
    flexDirection: "column" as const,
    gap: "18px",
  },
  inputGroup: {
    display: "flex",
    flexDirection: "column" as const,
    gap: "6px",
  },
  label: {
    color: "#ffffff",
    fontSize: "14px",
  },
  input: {
    width: "100%",
    padding: "10px 12px",
    borderRadius: "6px",
    border: "1px solid #3a3a3a",
    backgroundColor: "#2a2a2a",
    color: "#ffffff",
    fontSize: "15px",
    outline: "none",
    transition: "border-color 0.3s",
		boxSizing: "border-box" as const
  },
	passwordInput: {
    width: "100%",
    padding: "10px 40px 10px 12px",
    borderRadius: "6px",
    border: "1px solid #3a3a3a",
    backgroundColor: "#2a2a2a",
    color: "#ffffff",
    fontSize: "15px",
    outline: "none",
    boxSizing: "border-box" as const,
  },
  passwordContainer: {
    position: "relative" as const,
    display: "flex",
    alignItems: "center",
  },
  togglePassword: {
    position: "absolute" as const,
    right: "10px",
    top: "50%",
    transform: "translateY(-50%)",
    background: "none",
    border: "none",
    color: "#a0a0a0",
    fontSize: "13px",
    cursor: "pointer",
    padding: "0",
  },
  button: {
    width: "100%",
    padding: "12px",
    borderRadius: "6px",
    border: "none",
    backgroundColor: "#ffffff",
    color: "#000000",
    fontSize: "16px",
    fontWeight: "bold" as const,
    cursor: "pointer",
    transition: "background 0.3s, transform 0.2s",
  },
  forgotPassword: {
    color: "#a0a0a0",
    textAlign: "center" as const,
    display: "block",
    marginTop: "20px",
    fontSize: "14px",
    textDecoration: "none",
  },
};

export default RegisterPageComponente;
