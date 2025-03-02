require "0_utils"

students = {}
studentCount = 0
TARGET_STUDENTS = 5

print("Загрузка groups.lua...")
local success, err = pcall(function() dofile("groups.lua") end)
if not success then
    print("Ошибка загрузки groups.lua: " .. err)
end
if not groups or #groups == 0 then
    print("groups.lua пуст или не загружен")
    groups = {}
end

wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
wrk.headers["Host"] = "localhost:8080"

function response(status, headers, body)
    if status == 200 or status == 201 then
        local id = body:match('"id"%s*:%s*"([%w%-]+)"')
        if id then
            table.insert(students, id)
            studentCount = studentCount + 1
            local file = io.open("students.lua", "a")
            if file then
                file:write(string.format('"%s",\n', id))
                if studentCount >= TARGET_STUDENTS then
                    file:write("}\n")
                end
                file:close()
            end
        else
            print("Ошибка: не удалось извлечь ID из ответа: " .. body)
        end
    else
        print("Ошибка сервера: статус " .. status .. ", тело: " .. body)
    end
end

function request()
    if studentCount < TARGET_STUDENTS then
        if #groups == 0 then
            print("Ошибка: нет доступных групп")
            return nil
        end
        local path = "/api/students"
        local groupId = groups[math.random(1, #groups)]
        local body = string.format([[
        {
          "firstName": "%s",
          "lastName": "%s",
          "birthDate": "%s",
          "group": {"id": "%s"},
          "email": "%s",
          "phone": "%s"
        }
        ]], randomString(5), randomString(7), randomDate(), groupId, randomEmail(), randomPhone())
        return wrk.format("POST", path, nil, body)
    end
    return nil
end

local file = io.open("students.lua", "w")
if file then
    file:write("students = {\n")
    file:close()
end