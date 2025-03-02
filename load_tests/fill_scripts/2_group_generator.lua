require "0_utils"

groups = {}
groupCount = 0
TARGET_GROUPS = 5

wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
wrk.headers["Host"] = "localhost:8080"

function response(status, headers, body)
    if status == 200 or status == 201 then
        local id = body:match('"id"%s*:%s*"([%w%-]+)"')
        if id then
            table.insert(groups, id)
            groupCount = groupCount + 1
            local file = io.open("groups.lua", "a")
            if file then
                file:write(string.format('"%s",\n', id))
                if groupCount >= TARGET_GROUPS then
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
    if groupCount < TARGET_GROUPS then
        local path = "/api/groups"
        local body = string.format([[
        {
          "name": "%s",
          "years": %d,
          "faculty": "%s"
        }
        ]], randomString(4), math.random(1, 4), randomString(6))
        return wrk.format("POST", path, nil, body)
    end
    return nil
end

local file = io.open("groups.lua", "w")
if file then
    file:write("groups = {\n")
    file:close()
end