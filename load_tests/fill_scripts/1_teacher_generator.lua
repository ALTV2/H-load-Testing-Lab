require "0_utils"

teachers = {}
teacherCount = 0
TARGET_TEACHERS = 5

wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
wrk.headers["Host"] = "localhost:8080"

function response(status, headers, body)
    if status == 200 or status == 201 then
        local id = body:match('"id"%s*:%s*"([%w%-]+)"')
        if id then
            table.insert(teachers, id)
            teacherCount = teacherCount + 1
            local file = io.open("teachers.lua", "a")
            if file then
                file:write(string.format('"%s",\n', id))
                if teacherCount >= TARGET_TEACHERS then
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
    if teacherCount < TARGET_TEACHERS then
        local path = "/api/teachers"
        local body = string.format([[
        {
          "firstName": "%s",
          "lastName": "%s",
          "email": "%s",
          "phone": "%s"
        }
        ]], randomString(5), randomString(7), randomEmail(), randomPhone())
        return wrk.format("POST", path, nil, body)
    end
    return nil
end

local file = io.open("teachers.lua", "w")
if file then
    file:write("teachers = {\n")
    file:close()
end