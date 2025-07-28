# 🧪 Assignment 3 – Web App Performance, Resilience & Security  
**Course**: CSCI 5709 - Advanced Web Development  
**Author**: Sakthi Sharan M (B01012281)  
**Submission Date**: July 27, 2025  

---

## 📘 Project Overview

This project showcases a systematic approach to improving the **performance**, **resilience**, **security**, and **monitoring** of a web application.  
We selected the **DineConnect** platform – a restaurant booking and review system – and optimized its backend (Spring Boot + MongoDB) and frontend (React) for a production-like workload.

---

## 🛠️ Features Tested and Optimized

| Feature | Method | Endpoint |
|--------|--------|-----------|
| Fetch all restaurants | `POST` | `/api/restaurants/filter` |
| Bookings overview     | `GET`  | `/api/bookings` |
| User login            | `POST` | `/api/auth/login` |

---

## 🧪 Sections Included

### 1️⃣ Baseline Performance Testing (Apache JMeter)
- Simulated user load (10 & 50 users)
- Metrics captured: response time, throughput, latency
- Bottlenecks identified for login and restaurant filtering

### 2️⃣ Optimization Techniques
#### Frontend:
- Code-splitting with lazy loading
- Bundle compression using Brotli/Gzip

#### Backend:
- MongoDB compound indexing
- In-memory caching with Spring Cache

### 3️⃣ Post-Optimization Benchmarking
- Load testing rerun
- 10–12% improvement in average response time
- Higher throughput, stable error rates

### 4️⃣ Security Assessment (OWASP ZAP)
- Scanned with ZAP Docker CLI
- Initial vulnerabilities:
  - CORS misconfiguration
  - Actuator information exposure
  - HTTP-only site
- Fixes implemented in Spring Boot configuration

### 5️⃣ Monitoring Setup (Prometheus + Grafana)
- Actuator + Prometheus scraping
- Custom Grafana dashboard with:
  - CPU & memory metrics
  - API request latency
  - JVM stats and error rate

---

## 🔧 Tools Used

- 🧪 **Apache JMeter** – Load testing
- 🔐 **OWASP ZAP** – Vulnerability scanning
- 📊 **Prometheus + Grafana** – Monitoring dashboards
- ☕ **Spring Boot**, **MongoDB**, **React** – App stack

---


## 📈 Performance Result Summary

| Load Type | Avg Response Time | Throughput (req/sec) | Error % |
|-----------|-------------------|----------------------|---------|
| Before (Light) | 218ms | 1.36 | 0.00% |
| After (Light)  | 206ms | 3.27 | 0.00% |
| Before (Moderate) | 252ms | 1.36 | 0.00% |
| After (Moderate)  | 216ms | 3.27 | 0.00% |

---
