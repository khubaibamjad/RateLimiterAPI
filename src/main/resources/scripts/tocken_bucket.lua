local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local requested = tonumber(ARGV[3])
local now = tonumber(ARGV[4])

local bucket = redis.call("HMGET", key ,"tokens", "timestamp")
local tokens = tonumber(bucket[1])
local last_refill = tonumber(bucket[2])

if tokens == nil then
	token = capacity
	last_refill = now

end

local elapsed_seconds = Math.max(0, now - last_refill /1000)
local refilled = tokens + (elsapsed_seonds * refill_rate)
tokens = Math.min(capacity, refilled)

if tokens >= requested then
	tokens = tokens - requested
	allowed = 1

	else
	local deficit = request - tokens
	retry_after_millis = math.ciel((deficit/refill_rate)*1000)

	end

