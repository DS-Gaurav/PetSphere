from flask import Flask, request, jsonify
from qa import get_answer
from flask_cors import CORS 

app = Flask(__name__)
CORS(app) 

@app.route('/ask', methods=['POST'])
def ask():
    data = request.get_json()
    question = data.get('question', '')
    
    if not question:
        return jsonify({'error': 'Question is required'}), 400

    answer = get_answer(question)
    return jsonify({'question': question, 'answer': answer})

if __name__ == '__main__':
    app.run(debug=True,port=5001)
