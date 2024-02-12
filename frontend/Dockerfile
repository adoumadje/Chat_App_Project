# Stage 1: Build the angular app
FROM node:18.18-alpine AS build

WORKDIR /app

COPY . .

RUN npm install

RUN npm install -g @angular/cli@16.2.9

RUN ng build --configuration production


# Stage 2: Serve the built angular app using NGINX
FROM nginx:alpine

COPY --from=build /app/dist/frontend  /usr/share/nginx/html

EXPOSE 80

CMD [ "nginx", "-g", "daemon off;" ]