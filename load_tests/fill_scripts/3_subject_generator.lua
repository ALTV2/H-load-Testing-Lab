require "0_utils"

subjects = {}
subjectCount = 0
TARGET_SUBJECTS = 5

print("Загрузка teachers.lua...")
local success, err = pcall(function() dofile("teachers.lua") end)
if not success then
    print("Ошибка загрузки teachers.lua: " .. err)
end
if not teachers or #teachers == 0 then
    print("teachers.lua пуст или не загружен")
    teachers = {}
end

wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
wrk.headers["Host"] = "localhost:8080"

function response(status, headers, body)
    if status == 200 or status == 201 then
        local id = body:match('"id"%s*:%s*"([%w%-]+)"')
        if id then
            table.insert(subjects, id)
            subjectCount = subjectCount + 1
            local file = io.open("subjects.lua", "a")
            if file then
                file:write(string.format('"%s",\n', id))
                if subjectCount >= TARGET_SUBJECTS then
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
    if subjectCount < TARGET_SUBJECTS then
        if #teachers == 0 then
            print("Ошибка: нет доступных учителей")
            return nil
        end
        local path = "/api/subjects"
        local teacherId = teachers[math.random(1, #teachers)]
        local body = string.format([[
        {
          "name": "%s",
          "teacher": {"id": "%s"}
        }
        ]], randomString(6), teacherId)
        return wrk.format("POST", path, nil, body)
    end
    return nil
end

local file = io.open("subjects.lua", "w")
if file then
    file:write("subjects = {\n")
    file:close()
end