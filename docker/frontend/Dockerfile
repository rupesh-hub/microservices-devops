FROM node:20 as build-stage

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build --prod

FROM nginx:alpine

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Remove the default configuration
RUN rm /etc/nginx/conf.d/default.conf

# Copy the Angular build output to the Nginx HTML directory
COPY --from=build-stage /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80

# cd frontend
# docker build -t rupesh1997/frontend:1.0.0 -f ../docker/frontend/Dockerfile .
# docker run -d --network devops -p 8080:80 --name frontend-service  rupesh1997/frontend:1.0.0