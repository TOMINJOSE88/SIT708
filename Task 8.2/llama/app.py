from flask import Flask, request, jsonify, render_template_string
import requests
import json

app = Flask(__name__)

@app.route('/')
def index():
    return '''
        <h2 style="color:#FFD700; font-family:sans-serif;">🍽️ AI Recipe Generator</h2>
        <form method="post" action="/recipe">
            <label style="color:#DDD;">Enter ingredients:</label>
            <input name="ingredients" style="width:300px; padding:8px; margin:10px;" required>
            <input type="submit" value="Generate Recipe" style="padding:8px 16px; background-color:#FFD700; border:none; font-weight:bold;">
        </form>
    '''

@app.route('/generateRecipe', methods=['POST'])
def generate_recipe_for_android():
    try:
        data = request.get_json()
        ingredients = data.get('ingredients', '').strip()
        print("✅ Received ingredients (from Android):", ingredients)

        if not ingredients:
            return jsonify({"error": "No ingredients provided"}), 400

        prompt = f"Create a healthy recipe using: {ingredients}. Provide the recipe name and step-by-step instructions."

        response = requests.post(
            "http://localhost:11434/api/generate",
            json={"model": "llama2", "prompt": prompt},
            stream=True
        )

        print("🔄 Ollama response status:", response.status_code)

        result_text = ""
        for line in response.iter_lines():
            if line:
                chunk = json.loads(line.decode("utf-8"))
                if "response" in chunk:
                    result_text += chunk["response"]

        return jsonify({"recipe": result_text.strip()})

    except Exception as e:
        print("❌ Exception:", e)
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
