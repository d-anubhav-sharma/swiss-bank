# swiss-bank-user-interface/Dockerfile

# Use an official Node image to build the application
FROM node:20 AS builder
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
RUN npm run build:docker

# Use an official Nginx image to serve the built application
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
