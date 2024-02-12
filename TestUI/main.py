from chatter.chatter import Chatter


if __name__ == '__main__':
    with Chatter() as bot:
        bot.land_first_page()
        bot.register(fullname='Fede Valverde',
                     email='fede.valverde@fmail.com',
                     password='Password1234'
                     )
        bot.reset_password(
            email='fede.valverde@fmail.com',
            new_password='Password1234#'
        )
        bot.login(email='fede.valverde@fmail.com',
                  password='Password1234#')
        bot.send_public_message(name='Fede Valverde')
        bot.logout()


    input("Press ENTER to close the browser...")