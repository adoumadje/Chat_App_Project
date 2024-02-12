import os
import time

from selenium import webdriver
import chatter.constants as const
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class Chatter(webdriver.Chrome):
    def __init__(self, driver_path=r"C:\selenium_drivers", teardown=False):
        self.driver_path = driver_path
        self.teardown = teardown
        os.environ['PATH'] += self.driver_path
        super(Chatter, self).__init__()

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.teardown:
            self.quit()

    def land_first_page(self):
        self.get(const.BASE_URL)
        self.maximize_window()

    def register(self, fullname, email, password):
        register_link = self.find_element(By.CSS_SELECTOR, 'a[href="/register"]')
        register_link.click()
        fullname_input = self.find_element(By.CSS_SELECTOR, 'input[name="fullname"]')
        fullname_input.send_keys(fullname)
        email_input = self.find_element(By.CSS_SELECTOR, 'input[name="email"]')
        email_input.send_keys(email)
        password_input = self.find_element(By.CSS_SELECTOR, 'input[name="password"]')
        password_input.send_keys(password)
        confirm_pass_input = self.find_element(By.CSS_SELECTOR, 'input[name="confirmPass"]')
        confirm_pass_input.send_keys(password)
        submit_btn = self.find_element(By.CSS_SELECTOR, 'p-button[type="submit"]')
        submit_btn.click()

    def login(self, email, password):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/login')
        )
        email_input = self.find_element(By.CSS_SELECTOR, 'input[name="email"]')
        email_input.send_keys(email)
        password_input = self.find_element(By.CSS_SELECTOR, 'input[name="password"]')
        password_input.send_keys(password)
        submit_btn = self.find_element(By.CSS_SELECTOR, 'p-button[type="submit"]')
        submit_btn.click()

    def reset_password(self, email, new_password):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/login')
        )
        forgot_link = self.find_element(By.CSS_SELECTOR, 'a[href="/forgot-password"]')
        forgot_link.click()
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/forgot-password')
        )
        email_input = self.find_element(By.CSS_SELECTOR,
                                        'input[formcontrolname="email"]')
        email_input.send_keys(email)
        submit_btn = self.find_element(By.CSS_SELECTOR, 'p-button[type="submit"]')
        submit_btn.click()
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/reset-password')
        )
        password_input = self.find_element(By.CSS_SELECTOR,
                                           'input[formcontrolname="newPass"]')
        password_input.send_keys(new_password)
        confirm_pass_input = self.find_element(By.CSS_SELECTOR,
                                               'input[formcontrolname="confirmPass"]')
        confirm_pass_input.send_keys(new_password)
        submit_btn = self.find_element(By.CSS_SELECTOR, 'p-button[type="submit"]')
        submit_btn.click()

    def send_public_message(self, name):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/chat')
        )
        time.sleep(2)
        chatroom = self.find_element(By.CSS_SELECTOR, 'div.chatroom')
        chatroom.click()
        message_box = self.find_element(By.CSS_SELECTOR,
                                        'input[formcontrolname="textMessage"]')
        message = "Hello, I'm " + name
        message_box.send_keys(message)
        send_btn = self.find_element(By.CSS_SELECTOR, 'button[type="submit"]')
        send_btn.click()

    def logout(self):
        WebDriverWait(self, 30).until(
            EC.url_to_be(const.BASE_URL + '/chat')
        )
        logout_icon = self.find_element(By.CSS_SELECTOR, 'i[class="pi pi-sign-out ml-4"]')
        logout_icon.click()
        time.sleep(2)
        confirm_btn = self.find_element(By.CSS_SELECTOR, 'span[class="p-button-label ng-star-inserted"]')
        confirm_btn.click()