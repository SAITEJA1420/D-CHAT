from flask import Flask, request
from flask_sqlalchemy import SQLAlchemy
from flask_socketio import SocketIO
import socketio
import jwt
from datetime import datetime, timedelta
from werkzeug.security import check_password_hash, generate_password_hash

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///dchat.db'
db = SQLAlchemy(app)
socketio = SocketIO(app)
app.config['SECRET_KEY'] = 'Lucky_1420_PrO'


@app.route('/')
def hello():
    return {"message": "Flask is running"}

@app.route('/allUsers')
def all_users():
    users = User.query.all()
    user_list = [{'id': user.id, 'username': user.username} for user in users]
    return {'success': True, 'users': user_list}, 200

@app.route('/signup', methods = ['POST'])
def signup():
    data = request.get_json()
    username = data['username']
    password = data['password']
    if User.query.filter_by(username=username).first():
        return {"success": False, "message": "Username already taken"}, 200
    hashed_password = generate_password_hash(password)
    new_user = User(username= username, password= hashed_password)
    db.session.add(new_user)
    db.session.commit()
    return {"success": True, "message": "Log in your account."}

@app.route('/login', methods = ['POST'])
def login():
    data = request.get_json()
    username = data['username']
    password = data['password']
    user = User.query.filter_by(username = username).first()
    if user is not None:
        if check_password_hash(user.password, password):
            token = jwt.encode({'user_id': user.id, 'exp': datetime.utcnow() + timedelta(days=7)}, app.config['SECRET_KEY'], algorithm='HS256')
            return{"success":True, "message": "Login sucessfull", 'token': token, "userid": user.id}
        else:
            return{"success": False, "message": "Password was incorrect"}, 200
    return{"success": False, "message": "Username not found"}, 200

def verify_token(token):
    try:
        decoded_token = jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])  # Might throw error if expired
        return decoded_token['user_id']
    except:
        return None  # Catch the error, return None instead
    
@socketio.on('send_message')
def send_message(data):
    
    if isinstance(data, str):
        import json
        data = json.loads(data)
    
    sender_id = data['sender_id']
    receiver_id = data['receiver_id']
    content = data['content']
    
    user_id = verify_token(data['token'])
    
    if user_id == sender_id:
        try:
            new_message = Message(sender_id=sender_id, receiver_id=receiver_id, content=content)
            db.session.add(new_message)
            db.session.commit()

            # Add this - emit to receiver
            socketio.emit('receive_message', {
                'sender_id': sender_id,
                'content': content,
                'timestamp': str(new_message.timestamp)
            }, to=receiver_id)
            print(f"Emitted to receiver {receiver_id}")

        except Exception as e:
            db.session.rollback()
    else:
        return None

@app.route('/chat-history/<sender_id>/<receiver_id>')
def chat_history(sender_id, receiver_id):
    old_chat = messages = Message.query.filter(
        ((Message.sender_id == sender_id) & (Message.receiver_id == receiver_id)) |
        ((Message.sender_id == receiver_id) & (Message.receiver_id == sender_id))
    ).order_by(Message.timestamp).all()
    messages = [{'sender_id': message.sender_id,
            'receiver_id': message.receiver_id,
            'content': message.content,
            'timestamp': message.timestamp} for message in old_chat]
    return {'success':True, 'messages': messages}, 200

#Users model (table structure)
class User(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    username = db.Column(db.String(80), unique = True, nullable = False)
    password = db.Column(db.String(120), nullable = False)

# Message model (table structure)
class Message(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    sender_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    receiver_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    content = db.Column(db.String(500), nullable=False)
    timestamp = db.Column(db.DateTime, default=db.func.now())

with app.app_context():
    db.create_all()
if __name__ == '__main__':
    socketio.run(app, debug=True, port=5000, allow_unsafe_werkzeug=True)
