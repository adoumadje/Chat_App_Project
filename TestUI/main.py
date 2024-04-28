from chatter.chatter import Chatter

FULLNAME = 'Eduardo Camavinga'
EMAIL = 'eduardo.camavinga@fmail.com'
PASSWORD = 'Password1234#'
WRONG_PASSWORD = 'Password1234'

if __name__ == '__main__':
    with Chatter() as bot:
        bot.land_first_page()
        '''
        bot.register(fullname=FULLNAME,
                     email=EMAIL,
                     password=PASSWORD
                     )
        
        bot.reset_password(
            email='fede.valverde@fmail.com',
            new_password=PASSWORD
        )
        '''
        bot.login(email=EMAIL,
                  password=PASSWORD)
        bot.send_public_message(name=FULLNAME)
        bot.logout()


    input("Press ENTER to close the browser...")