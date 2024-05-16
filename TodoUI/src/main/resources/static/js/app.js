const result = document.getElementById("result");
const addBtn = document.getElementById("add-btn");
const todo = document.getElementById("todo");

window.onload = async () => {
    await loadTodosAndShow();
}

async function loadTodosAndShow() {
    let response = await fetch("http://localhost:9095/todo/get-all");
    let todos = await response.json();

    result.innerText = "";
    todos.forEach(todo => {

        let li = createTag("li", ["list-group-item", "d-flex", "flex-row", "justify-content-between", "align-items-center"]);
        let span = createTag("span", []);
        span.innerText = todo.todo;

        let deleteBtn = createTag("button", ["btn", "btn-danger"]);
        deleteBtn.innerText = "Del";
        deleteBtn.id = "del-btn";
        deleteBtn.dataset.id = todo.id;
        deleteBtn.onclick = async () => {
            let id = deleteBtn.dataset.id;
            let response = await fetch(`http://localhost:9095/todo/delete/${id}`)
            let data = await response.json();
            console.log(data)

            await loadTodosAndShow();
        }


        li.append(span);
        li.append(deleteBtn);

        result.append(li);
    })
}

addBtn.onclick = async () => {
    let todoText = todo.value;
    if (todoText.length === 0) {
        return;
    }

    let body = {
        id: 0,
        todo: todoText,
        date: ""
    }

    let response = await fetch("http://localhost:9095/todo/save", {
        method: "POST",
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    let data = await response.json();
    console.log(data)

    await loadTodosAndShow();

    todo.value = "";
}

function createTag(name, classArr) {
    let tag = document.createElement(name);
    classArr.forEach(cls => {
            tag.classList.add(cls)
        }
    );

    return tag;
}